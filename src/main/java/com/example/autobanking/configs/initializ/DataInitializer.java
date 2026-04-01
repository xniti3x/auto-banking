package com.example.autobanking.configs.initializ;

import com.example.autobanking.tokens.entity.Token;
import org.openapitools.client.ApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.autobanking.users.entity.User;
import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository,ApiClient client) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔥 DataInitializer STARTED");
        if(userRepository.count() == 0) {

            Token token = Token.builder()
                    .access("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzc1MTI4NDM4LCJqdGkiOiI3ZjJjMWQxYTk4MTE0ZjZmOThkMDhlZDJkZmU5MzhhZCIsInV1aWQiOiI5NTk4ZGNjMy00NjMxLTQyNDItOTgyOC05Mjc3YjQ5OTNjZjgiLCJhbGxvd2VkX2NpZHJzIjpbIjAuMC4wLjAvMCIsIjo6LzAiXX0.fO6qNI_A8me6xq8Bri1mYt6UaeccWheL0ufFZv7ONZQ")
                    .accessExpires(86400)
                    .refresh("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTc3NzIwMjU1MSwianRpIjoiODIwNTRiNTU3ZjI5NGQxMDkxMjQwYjc0N2JiMzk3YjIiLCJ1dWlkIjoiOTU5OGRjYzMtNDYzMS00MjQyLTk4MjgtOTI3N2I0OTkzY2Y4IiwiYWxsb3dlZF9jaWRycyI6WyIwLjAuMC4wLzAiLCI6Oi8wIl19.ZPKaKqbBlJHc8FW4RkjdvntPPqGnJ9HrtwYdfiSjRRQ")
                    .refreshExpires(2592000).build();

            UserGoCardlessDetails details = UserGoCardlessDetails.builder()
                    .secretId("default-secret")
                    .secretKey("default-key")
                    .token(token)
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
}