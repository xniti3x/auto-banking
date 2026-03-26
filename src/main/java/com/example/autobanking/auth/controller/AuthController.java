package com.example.autobanking.auth.controller;

import com.example.autobanking.auth.model.LoginRequest;
import com.example.autobanking.auth.model.LoginResponse;
import com.example.autobanking.auth.model.RegisterRequest;
import com.example.autobanking.auth.service.AuthService;
import com.example.autobanking.bank.entity.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Operations related to Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return new LoginResponse(token);
    }

    // POST /auth/register
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword());
    }
}