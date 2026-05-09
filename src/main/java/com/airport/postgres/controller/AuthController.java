package com.airport.postgres.controller;

import com.airport.postgres.dto.LoginRequest;
import com.airport.postgres.dto.LoginResponse;
import com.airport.postgres.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    
    @GetMapping("/hash")
    public String gerarHash() {
        return new BCryptPasswordEncoder().encode("123456");
    }
}