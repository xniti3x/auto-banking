package com.example.autobanking.token.controller;


import com.example.autobanking.token.service.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.openapitools.client.model.JWTRefreshRequest;
import org.openapitools.client.model.SpectacularJWTObtain;
import org.openapitools.client.model.SpectacularJWTRefresh;
import org.springframework.web.bind.annotation.*;

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
    public SpectacularJWTObtain createToken(@RequestBody JWTObtainPairRequest request) throws ApiException {
        return tokenService.createToken(request);
    }

    // POST /token/refresh/
    @PostMapping("/refresh")
    public SpectacularJWTRefresh refreshToken(@RequestBody JWTRefreshRequest request) throws ApiException {
        return tokenService.refreshToken(request);
    }
}