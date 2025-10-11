package com.log430.brockerx.controller;

import com.log430.brockerx.dto.DepositRequestDto;
import com.log430.brockerx.dto.DepositResponseDto;
import com.log430.brockerx.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users/{userId}/wallet")
public class WalletController {

    private final WalletService walletService;

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/deposit") public ResponseEntity<DepositResponseDto> deposit(@PathVariable Long userId,
                                                                               @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
                                                                               @Valid @RequestBody DepositRequestDto req,
                                                                               Principal principal) {

        log.info("User: {} has deposited : {}", principal.getName(), req.getAmount());

        DepositResponseDto res = walletService.deposit(userId, req.getAmount(), idempotencyKey);
        return ResponseEntity.ok(res);
    }
}

