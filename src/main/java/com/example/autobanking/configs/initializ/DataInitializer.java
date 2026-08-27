package com.example.autobanking.configs.initializ;

import com.example.autobanking.users.entity.User;
import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Creates a sandbox-only account for local development. It deliberately does
 * not contain GoCardless credentials or tokens.
 */
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() != 0) {
            return;
        }

        UserGoCardlessDetails details = UserGoCardlessDetails.builder()
                .institutionId("SANDBOXFINANCE_SFIN0000")
                .build();

        User admin = User.builder()
                .email("admin123")
                .passwordHash("$2a$10$bORFGwX5ooZoB0xUWYIMmeMZtFHTd2ZiRGg6WlYZJYj3fDaxgEbVW") // admin123
                .name("admin")
                .goCardlessDetails(details)
                .build();

        userRepository.save(admin);
    }
}
