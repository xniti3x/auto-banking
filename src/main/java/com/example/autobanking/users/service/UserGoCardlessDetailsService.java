package com.example.autobanking.users.service;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.mapper.UserGoCardlessDetailsMapper;
import com.example.autobanking.users.model.UserGoCardlessDetailsDto;
import com.example.autobanking.users.repository.UserGoCardlessDetailsRepository;

import jakarta.transaction.Transactional;

@Service
public class UserGoCardlessDetailsService {
    
        private final UserGoCardlessDetailsRepository userGoCardlessDetailsRepository;

    public UserGoCardlessDetailsService(UserGoCardlessDetailsRepository userGoCardlessDetailsRepository) {
        this.userGoCardlessDetailsRepository = userGoCardlessDetailsRepository;
    }

    @Transactional
    public void updateGoCardlessDetailsForLoggedInUser(UserGoCardlessDetailsDto userGoCardlessDetailsDto) {
        Optional<UserGoCardlessDetails> details = userGoCardlessDetailsRepository.findById(userGoCardlessDetailsDto.getId());
        details.ifPresent(dt->{
            UserGoCardlessDetailsMapper mapper = Mappers.getMapper(UserGoCardlessDetailsMapper.class);
            userGoCardlessDetailsRepository.save(mapper.toEntity(userGoCardlessDetailsDto));
    });
    }
    
    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
