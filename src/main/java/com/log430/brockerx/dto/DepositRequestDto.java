package com.log430.brockerx.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.util.Map;

/**
 * DTO pour déposer de l'argent dans le wallet d'un utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestDto {
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Double amount;
    private Map<String, Object> metadata;
}
