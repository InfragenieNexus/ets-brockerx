package com.log430.brockerx.controller;

import com.log430.brockerx.dto.LoginRequestDto;
import com.log430.brockerx.dto.UserRequestDto;
import com.log430.brockerx.dto.UserResponseDto;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.mapper.UserMapper;
import com.log430.brockerx.service.JwtService;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

// ======= USER CONTROLLER =======
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    // GET /api/users/{id}
    @GetMapping("/{id}") public CompletableFuture<ResponseEntity<UserResponseDto>> getUser(@PathVariable Long id) {
        return userService.findByIdAsync(id).thenApply(user -> {
            if (user != null)
                return ResponseEntity.ok(userMapper.toDto(user));
            else
                return ResponseEntity.notFound().build();
        });
    }

    @PostMapping("/login") public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }


    // POST /api/users
    @PostMapping public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        User savedUser = userService.save(user);
        return ResponseEntity.status(201).body(userMapper.toDto(savedUser));
    }

    @PostMapping("/send-otp") public ResponseEntity<String> sendOtp(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        otpService.sendOTP(user);
        return ResponseEntity.ok("OTP envoyé !");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String code) {
        User user = userService.findByEmail(email);
        if (user == null)
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");

        boolean ok = otpService.verifyCode(user, code);
        if (ok)
            return ResponseEntity.ok("OTP validé !");
        else
            return ResponseEntity.status(401).body("OTP invalide");
    }


}
