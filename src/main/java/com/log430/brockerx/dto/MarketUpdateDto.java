package com.log430.brockerx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketUpdateDto {
    private String symbol;
    private double lastPrice;
    private double open;
    private double high;
    private double low;
    private long volume;
    private Instant timestamp;
}
