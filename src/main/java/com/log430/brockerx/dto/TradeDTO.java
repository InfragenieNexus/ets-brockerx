package com.log430.brockerx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {
    private String symbol;
    private double price;
    private double quantity;
    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyerId;
    private Long sellerId;
    private Instant timestamp;
}
