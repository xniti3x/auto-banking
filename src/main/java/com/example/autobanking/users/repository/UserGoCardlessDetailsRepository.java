package com.example.autobanking.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.users.entity.UserGoCardlessDetails;

public interface UserGoCardlessDetailsRepository  extends JpaRepository<UserGoCardlessDetails, Long>{
    
}