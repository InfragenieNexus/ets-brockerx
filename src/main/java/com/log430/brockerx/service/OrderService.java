package com.log430.brockerx.service;

import com.log430.brockerx.dto.OrderRequestDto;
import com.log430.brockerx.dto.OrderResponseDto;
import com.log430.brockerx.entity.Order;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.OrderRepository;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final MatchingEngineService matchingEngineService;
    private final PositionService positionService;

    public OrderResponseDto placeOrder(OrderRequestDto request, String idempotencyKey) {

        User user = userRepository.findById(request.getUserId()).orElseThrow();
//        Optional<Order> existing = orderRepository.findByIdempotencyKey(idempotencyKey);//Could be opti
//        if (existing.isPresent()) {
//            Order order = existing.get();
//            return new OrderResponseDto(order.getId().toString(), order.getStatus(), null, order.getCreatedAt());
//        }

        // ---------- Pré-trade checks ----------
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return new OrderResponseDto(null, "REJECT", "Quantity must be > 0", Instant.now());
        }

        if (request.getSymbol() == null || request.getSymbol().isEmpty()) {
            return new OrderResponseDto(null, "REJECT", "Symbol is required", Instant.now());
        }

        if ("LIMIT".equalsIgnoreCase(request.getType()) && (request.getPrice() == null || request.getPrice() <= 0)) {
            return new OrderResponseDto(null, "REJECT", "Price required for LIMIT order", Instant.now());
        }
        if (!walletService.checkWalletFunds(user.getId(), request.getPrice())) {
            return new OrderResponseDto(null, "REJECT", "Insufficient margin for order", Instant.now());
        }
        // Si c'est un SELL, vérifier que l'utilisateur possède assez de positions
        if ("SELL".equalsIgnoreCase(request.getSide())) {
            double ownedQty = positionService.getPositionQuantity(user.getId(), request.getSymbol());
            if (ownedQty < request.getQuantity()) {
                return new OrderResponseDto(null, "REJECT",
                                            "Insufficient position for selling " + request.getQuantity() + " " +
                                            request.getSymbol(), Instant.now());
            }
        }


        // TODO: ajouter checks fonds, restrictions short-sell, tick size, etc.

        // ---------- Normalisation / horodatage ----------
        Instant now = Instant.now();
        Order order = new Order();
        order.setUser(user);
        order.setSymbol(request.getSymbol());
        order.setSide(request.getSide());
        order.setType(request.getType());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setTimeInForce(request.getTimeInForce());
        order.setStatus("ACK");
        order.setCreatedAt(now);

        // ---------- Persistance ----------
        Order savedOrder = orderRepository.save(order);

        matchingEngineService.processNewOrder(order);

        return new OrderResponseDto(savedOrder.getId().toString(), savedOrder.getStatus(), null, now);
    }
}
