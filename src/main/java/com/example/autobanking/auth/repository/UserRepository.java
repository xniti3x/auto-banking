package com.example.autobanking.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.autobanking.bank.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}