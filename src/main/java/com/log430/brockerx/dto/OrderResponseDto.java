package com.log430.brockerx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private String orderId;
    private String status;       // "ACK", "REJECT"
    private String reason;       // si rejeté
    private Instant timestamp;
}
