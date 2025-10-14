package com.log430.brockerx.service;

import com.log430.brockerx.dto.TradeDTO;
import com.log430.brockerx.entity.Order;
import com.log430.brockerx.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MatchingEngineService {

    private static final String SELL = "SELL";
    private static final String BUY = "BUY";

    private final OrderRepository orderRepository;
    private final TradeService tradeService;

    public MatchingEngineService(OrderRepository orderRepository, TradeService tradeService) {
        this.orderRepository = orderRepository;
        this.tradeService = tradeService;
    }

    @Transactional public void processNewOrder(Order newOrder) {
        log.info("Received new order: id={}, user={}, side={}, symbol={}, qty={}, price={}", newOrder.getId(),
                 newOrder.getUser() != null ? newOrder.getUser().getEmail() : "unknown", newOrder.getSide(),
                 newOrder.getSymbol(), newOrder.getQuantity(), newOrder.getPrice());

        List<Order> oppositeOrders;

        if (newOrder.getSide().equals(BUY)) {
            oppositeOrders = orderRepository.findBuyMatchingOrders(newOrder.getSymbol(), newOrder.getPrice());
        } else {
            oppositeOrders = orderRepository.findSellMatchingOrders(newOrder.getSymbol(), newOrder.getPrice());
        }


        double remainingQty = newOrder.getQuantity();

        for (Order o : oppositeOrders) {
            if (remainingQty <= 0)
                break;

            double matchedQty = Math.min(remainingQty, o.getQuantity());
            remainingQty -= matchedQty;
            o.setQuantity(o.getQuantity() - matchedQty);

            if (o.getQuantity() == 0)
                o.setStatus("WORKING");
            else
                o.setStatus("PARTIALLY_FILLED");

            orderRepository.save(o);

            TradeDTO trade = new TradeDTO();
            trade.setSymbol(newOrder.getSymbol());
            trade.setQuantity(matchedQty);
            trade.setPrice(o.getPrice());

            if (newOrder.getSide().equalsIgnoreCase("BUY")) {
                trade.setBuyerId(newOrder.getUser().getId());
                trade.setSellerId(o.getUser().getId());
            } else {
                trade.setBuyerId(o.getUser().getId());
                trade.setSellerId(newOrder.getUser().getId());
            }

            tradeService.handleTrade(trade);

            log.info("💱 Trade executed and settled: {} {} @ {} (buyer={}, seller={})", matchedQty, newOrder.getSymbol(),
                     o.getPrice(), trade.getBuyerId(), trade.getSellerId());
        }

        if (remainingQty > 0) {
            newOrder.setQuantity(remainingQty);
            newOrder.setStatus("ACK");
        } else {
            newOrder.setStatus("FILLED");
        }

        orderRepository.save(newOrder);
    }

}
