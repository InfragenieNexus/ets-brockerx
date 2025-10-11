package com.log430.brockerx.service;

import com.log430.brockerx.dto.TradeDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TradeService {

    private final WalletService walletService;
    private final PositionService positionService;
    private final Counter tradeSettledCounter;

    public TradeService(WalletService walletService, PositionService positionService, MeterRegistry meterRegistry) {
        this.walletService = walletService;
        this.positionService = positionService;
        this.tradeSettledCounter = Counter.builder("trades_settled_total").description(
                "Nombre total de trades exécutés").register(meterRegistry);
    }

    @Transactional public void handleTrade(TradeDTO trade) {

        log.info("handleTrade {}", trade);

        double totalAmount = trade.getPrice() * trade.getQuantity();

        // 💰 Transferts d'argent
        walletService.debit(trade.getBuyerId(), totalAmount);
        walletService.credit(trade.getSellerId(), totalAmount);

        // 📊 Mise à jour des positions
        positionService.addToPosition(trade.getBuyerId(), trade.getSymbol(), trade.getQuantity());
        positionService.removeFromPosition(trade.getSellerId(), trade.getSymbol(), trade.getQuantity());

        tradeSettledCounter.increment();

        // 🧾 Log de confirmation
        log.info("💱 Trade settled: {} {} @ {} between buyer={} and seller={}", trade.getQuantity(), trade.getSymbol(),
                 trade.getPrice(), trade.getBuyerId(), trade.getSellerId());
    }
}
