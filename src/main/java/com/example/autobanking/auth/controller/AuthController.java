package com.example.autobanking.auth.controller;

import com.example.autobanking.auth.service.AuthService;
import com.example.autobanking.bank.entity.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
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

    @GetMapping("/login/email/{email}/password/{password}")
    public LoginResponse login(@PathVariable String email, @PathVariable("password") String pass) {
        String token = authService.login(email, pass);
        return new LoginResponse(token);
    }

    // POST /auth/register
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword());
    }

        @GetMapping("/register1/email/{email}/password/{password}")
    public User register(@PathVariable String email, @PathVariable String password) {
        return authService.register(email, password);
    }
    // DTOs for requests/responses
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String email;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private final String token;
    }
}