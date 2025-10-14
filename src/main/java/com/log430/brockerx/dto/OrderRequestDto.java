package com.log430.brockerx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private String symbol;       // ex: "AAPL"
    private String side;         // "BUY" ou "SELL"
    private String type;         // "MARKET" ou "LIMIT"
    private Double quantity;    // nombre d’actions
    private Double price;        // uniquement si LIMIT
    private String timeInForce;  // "DAY", "IOC", "FOK"
    private String emailUser;
    private Long userId;
}
