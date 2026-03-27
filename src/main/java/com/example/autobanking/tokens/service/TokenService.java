package com.example.autobanking.tokens.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TokenApi;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.openapitools.client.model.JWTRefreshRequest;
import org.openapitools.client.model.SpectacularJWTObtain;
import org.openapitools.client.model.SpectacularJWTRefresh;
import org.springframework.stereotype.Service;

import com.example.autobanking.tokens.entity.Token;
import com.example.autobanking.tokens.repository.TokenRepository;

@Service
public class TokenService {

    private final ApiClient apiClient;
    private final TokenApi tokenApi;
    private final TokenRepository tokenRepository;

    public TokenService(ApiClient apiClient, TokenApi tokenApi, TokenRepository tokenRepository) {
        this.apiClient = apiClient;
        this.tokenApi = tokenApi;
        this.tokenRepository = tokenRepository;
    }

    // POST /token/new/
    public Token createToken(JWTObtainPairRequest request) throws ApiException {
        SpectacularJWTObtain spectacularToken = tokenApi.obtainNewAccessRefreshTokenPair(request);
        Token token = mapSpectacularJWTObtainToEntityToken(spectacularToken);
        
        if(token!=null){
            apiClient.setAccessToken(token.getAccess());
            return tokenRepository.save(token);
        }else{
            return null;
        }
    }

    // POST /token/refresh/
    public Token refreshToken(Token requestToken) throws ApiException {
        if(requestToken!=null && requestToken.getRefresh()!=null && !requestToken.getRefresh().isEmpty()){
            SpectacularJWTRefresh aNewAccessToken = tokenApi.getANewAccessToken(new JWTRefreshRequest().refresh(requestToken.getRefresh()));
            requestToken.setAccess(aNewAccessToken.getAccess());
            requestToken.setAccessExpires(aNewAccessToken.getAccessExpires());
            apiClient.setAccessToken(aNewAccessToken.getAccess());
            return tokenRepository.save(requestToken);
        }else{
            return null;
        }
    }

    private Token mapSpectacularJWTObtainToEntityToken(SpectacularJWTObtain spectacularToken) {
       return Token.builder()
        .access(spectacularToken.getAccess())
        .accessExpires(spectacularToken.getAccessExpires())
        .refresh(spectacularToken.getRefresh())
        .refreshExpires(spectacularToken.getRefreshExpires())
        .build();
    }
}