package com.log430.brockerx.service;

import com.log430.brockerx.entity.Position;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.PositionRepository;
import com.log430.brockerx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    @Transactional public void addToPosition(Long userId, String symbol, double qty) {
        if (qty <= 0) {
            log.warn("⚠️ Invalid quantity: {} (must be > 0)", qty);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));

        Position pos = positionRepository.findByUserIdAndSymbol(userId, symbol).orElseGet(() -> {
            Position newPos = new Position();
            newPos.setUser(user);
            newPos.setSymbol(symbol);
            newPos.setQuantity(0.0);
            return newPos;
        });

        double newQty = pos.getQuantity() + qty;
        pos.setQuantity(newQty);

        positionRepository.save(pos);

        log.info("📈 Position updated (BUY): user={}, symbol={}, newQty={}", user.getEmail(), symbol, newQty);
    }

    @Transactional public void removeFromPosition(Long userId, String symbol, double qty) {
        if (qty <= 0) {
            log.warn("⚠️ Invalid quantity: {} (must be > 0)", qty);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));

        Position pos = positionRepository.findByUserIdAndSymbol(userId, symbol).orElseThrow(
                () -> new IllegalArgumentException(
                        "No position found for user " + user.getEmail() + " and symbol " + symbol));

        double newQty = pos.getQuantity() - qty;

        if (newQty <= 0) {
            positionRepository.delete(pos);
            log.info("💀 Position closed: user={}, symbol={}, soldQty={}", user.getEmail(), symbol, qty);
        } else {
            pos.setQuantity(newQty);
            positionRepository.save(pos);
            log.info("📉 Position reduced: user={}, symbol={}, remainingQty={}", user.getEmail(), symbol, newQty);
        }
    }

    public double getPositionQuantity(Long id, String symbol) {

        return positionRepository.findByUserIdAndSymbol(id, symbol).orElseThrow().getQuantity();
    }
}
