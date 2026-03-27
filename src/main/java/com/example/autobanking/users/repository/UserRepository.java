package com.example.autobanking.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}