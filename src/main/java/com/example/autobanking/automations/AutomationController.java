package com.example.autobanking.automations;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/automation")
public class AutomationController {

    private final ConfigService configService;

    public AutomationController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/start")
    public void start() {
        configService.setAutomation(true);
    }

    @PostMapping("/stop")
    public void stop() {
        configService.setAutomation(false);
    }
}