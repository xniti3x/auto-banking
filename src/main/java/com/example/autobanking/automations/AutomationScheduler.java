package com.example.autobanking.automations;

import org.openapitools.client.ApiException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutomationScheduler {

    private final ConfigService configService;
    private final AutomationService service;

    public AutomationScheduler(ConfigService configService, AutomationService service) {
        this.configService = configService;
        this.service = service;
    }

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void run() throws ApiException {
        if (!configService.isAutomationEnabled()) {
            return;
        }
        service.executeAutomation();
    }
}