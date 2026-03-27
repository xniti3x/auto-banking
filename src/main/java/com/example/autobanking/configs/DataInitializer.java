package com.example.autobanking.configs;

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
            UserGoCardlessDetails details = UserGoCardlessDetails.builder()
                    .secretId("default-secret")
                    .secretKey("default-key")
                    .build();
           
            User admin = User.builder()
                    .email("admin")
                    .passwordHash("$2a$10$7sN5s6r9Q7Wz8v0KqGZ3QeQ7YzP6r5t9jZ1Wz7z5Jx8K1qV6rYk1K") // bcrypt hashed
                    .name("Admin")
                    .goCardlessDetails(details)
                    .build();
            userRepository.save(admin);
        }
    }
}