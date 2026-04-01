package com.example.autobanking.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.users.entity.UserGoCardlessDetails;

public interface UserGoCardlessDetailsRepository  extends JpaRepository<UserGoCardlessDetails, Long>{
    
}