package com.example.autobanking.users.service;

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
    private final UserGoCardlessDetailsMapper mapper;

    public UserGoCardlessDetailsService(
            UserGoCardlessDetailsRepository userGoCardlessDetailsRepository,
            UserGoCardlessDetailsMapper mapper) {
        this.userGoCardlessDetailsRepository = userGoCardlessDetailsRepository;
        this.mapper = mapper;
    }

@Transactional
public void updateGoCardlessDetailsForLoggedInUser(UserGoCardlessDetailsDto dto) {

    UserGoCardlessDetails existing = userGoCardlessDetailsRepository
        .findByUsername(getLoggedInUsername())
        .orElseThrow(() -> new RuntimeException("User details not found"));

    existing.setAgreementExpDate(dto.getAgreementExpDate());
    existing.setEndUserAgreementId(dto.getEndUserAgreementId());
    existing.setInstitutionId(dto.getInstitutionId());
    existing.setRequisitionId(dto.getRequisitionId());

    userGoCardlessDetailsRepository.save(existing);
}
    
    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public UserGoCardlessDetailsDto getUserGoCardlessDetails() {
    return userGoCardlessDetailsRepository
        .findByUsername(getLoggedInUsername())
        .map(mapper::toDto)
        .orElseThrow(() -> new RuntimeException("User details not found"));
    }
}
