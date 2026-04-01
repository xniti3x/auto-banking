package com.example.autobanking.automations;

import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.repository.UserGoCardlessDetailsRepository;
import com.example.autobanking.users.service.UserService;
import org.springframework.stereotype.Service;

@Service
class ConfigService {

    private final UserService userService;
    private UserGoCardlessDetailsRepository userGoCardlessDetailsRepository;
    public ConfigService(UserService userService,UserGoCardlessDetailsRepository userGoCardlessDetailsRepository){
        this.userService = userService;
        this.userGoCardlessDetailsRepository = userGoCardlessDetailsRepository;
    }

    public void setAutomation(boolean b) {
        UserGoCardlessDetails goCardlessDetails = userService.getLoggedInUser().getGoCardlessDetails();
        goCardlessDetails.setAutomationEnabled(b);
        userGoCardlessDetailsRepository.save(goCardlessDetails);
    }

    public boolean isAutomationEnabled() {
        return userService.getLoggedInUser().getGoCardlessDetails().isAutomationEnabled();
    }
}
