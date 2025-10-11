package com.log430.brockerx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO retourné après un dépôt réussi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponseDto {
    private Double newBalance; // nouveau solde du wallet
}
