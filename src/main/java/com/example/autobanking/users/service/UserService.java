package com.example.autobanking.users.service;

import org.springframework.stereotype.Service;

import com.example.autobanking.users.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}