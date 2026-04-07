package com.example.autobanking.users.service;

import com.example.autobanking.users.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.autobanking.users.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsernme(String username){
        return userRepository.findByEmail(username);
    }

    public User getLoggedInUser(){
        return findByUsernme(getLoggedInUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}