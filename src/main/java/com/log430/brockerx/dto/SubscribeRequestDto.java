package com.log430.brockerx.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscribeRequestDto {
    private List<String> symbols; // ex: ["AAPL", "GOOG"]
}
