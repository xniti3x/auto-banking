package com.example.autobanking.token.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TokenApi;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.openapitools.client.model.JWTRefreshRequest;
import org.openapitools.client.model.SpectacularJWTObtain;
import org.openapitools.client.model.SpectacularJWTRefresh;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenApi tokenApi;

    public TokenService() {
        ApiClient client = new ApiClient();
        this.tokenApi = new TokenApi(client);
    }

    // POST /token/new/
    public SpectacularJWTObtain createToken(JWTObtainPairRequest request) throws ApiException {
        return tokenApi.obtainNewAccessRefreshTokenPair(request);
    }

    // POST /token/refresh/
    public SpectacularJWTRefresh refreshToken(JWTRefreshRequest request) throws ApiException {
        return tokenApi.getANewAccessToken(request);
    }
}