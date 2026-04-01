package com.example.autobanking.tokens.controller;


import org.openapitools.client.ApiException;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.autobanking.tokens.entity.Token;
import com.example.autobanking.tokens.service.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/token")
@Tag(name = "Token", description = "Operations related to Token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // POST /token/new/
    @PostMapping("/new")
    public Token createToken(@RequestBody JWTObtainPairRequest request) throws ApiException {
        return tokenService.createToken(request);
    }

    // POST /token/refresh/
    @PostMapping("/refresh")
    public Token refreshToken() throws ApiException {
        return tokenService.refreshToken();
    }
}