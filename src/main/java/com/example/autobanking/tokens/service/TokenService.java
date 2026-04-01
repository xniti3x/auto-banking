package com.example.autobanking.tokens.service;

import com.example.autobanking.users.entity.User;
import com.example.autobanking.users.service.UserService;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TokenApi;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.openapitools.client.model.JWTRefreshRequest;
import org.openapitools.client.model.SpectacularJWTObtain;
import org.openapitools.client.model.SpectacularJWTRefresh;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.autobanking.tokens.entity.Token;
import com.example.autobanking.tokens.repository.TokenRepository;

import java.util.Optional;

@Service
public class TokenService {

    private final ApiClient apiClient;
    private final TokenApi tokenApi;
    private final TokenRepository tokenRepository;
    private final UserService userService;

    public TokenService(ApiClient apiClient, TokenApi tokenApi, TokenRepository tokenRepository, UserService userService) {
        this.apiClient = apiClient;
        this.tokenApi = tokenApi;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    // POST /token/new/
    public Token createToken(JWTObtainPairRequest request) throws ApiException {
        SpectacularJWTObtain spectacularToken = tokenApi.obtainNewAccessRefreshTokenPair(request);
        Token token = mapSpectacularJWTObtainToEntityToken(spectacularToken);
        
        if(token!=null){
            apiClient.setBearerToken(token.getAccess());
            return tokenRepository.save(token);
        }else{
            return null;
        }
    }

    // POST /token/refresh/
    public Token refreshToken() throws ApiException {
        User user = userService.findByUsernme(userService.getLoggedInUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Token token = user.getGoCardlessDetails().getToken();
        SpectacularJWTRefresh aNewAccessToken = tokenApi.getANewAccessToken(new JWTRefreshRequest().refresh(token.getRefresh()));

        token.setAccess(aNewAccessToken.getAccess());
        token.setAccessExpires(aNewAccessToken.getAccessExpires());
        apiClient.setBearerToken(aNewAccessToken.getAccess());

        return tokenRepository.save(token);
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