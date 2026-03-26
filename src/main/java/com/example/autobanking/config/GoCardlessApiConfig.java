package com.example.autobanking.config;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.api.AgreementsApi;
import org.openapitools.client.api.InstitutionsApi;
import org.openapitools.client.api.RequisitionsApi;
import org.openapitools.client.api.TokenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoCardlessApiConfig {

    @Bean
    public ApiClient apiClient() {
        ApiClient client = new ApiClient();
        return client;
    }

    @Bean
    public AgreementsApi agreementsApi(ApiClient apiClient) {
        return new AgreementsApi(apiClient);
    }

    @Bean
    public RequisitionsApi requisitionsApi(ApiClient apiClient) {
        return new RequisitionsApi(apiClient);
    }

    @Bean
    public InstitutionsApi institutionsApi(ApiClient apiClient) {
        return new InstitutionsApi(apiClient);
    }
    
    @Bean
    public AccountsApi accountsApi(ApiClient apiClient) {
        return new AccountsApi(apiClient);
    }

    @Bean
    public TokenApi tokenApi(ApiClient apiClient) {
        return new TokenApi(apiClient);
    }
}