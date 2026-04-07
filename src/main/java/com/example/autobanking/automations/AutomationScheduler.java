package com.example.autobanking.automations;

import org.openapitools.client.ApiException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutomationScheduler {

    private final AutomationService service;

    public AutomationScheduler(AutomationService service) {
        this.service = service;
    }

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void run() throws ApiException {
        if (!service.isAutomationEnabled()) {
            return;
        }
        service.executeAutomation();
    }
}