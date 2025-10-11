package com.log430.brockerx.controller;

import com.log430.brockerx.dto.OrderRequestDto;
import com.log430.brockerx.dto.OrderResponseDto;
import com.log430.brockerx.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);


    @PostMapping public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid OrderRequestDto request,
                                                                    @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
                                                                    Principal principal) {

        log.info("Order {} placed by User: {}", request.toString(), principal.getName());
        request.setEmailUser(principal.getName());
        OrderResponseDto response = orderService.placeOrder(request, idempotencyKey);
        if ("REJECT".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
