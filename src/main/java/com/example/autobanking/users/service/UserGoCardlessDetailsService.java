package com.example.autobanking.users.service;

import com.example.autobanking.users.entity.User;
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
    private final UserService userService;
    private final UserGoCardlessDetailsMapper mapper;

    public UserGoCardlessDetailsService(
            UserGoCardlessDetailsRepository userGoCardlessDetailsRepository,
            UserService userService,
            UserGoCardlessDetailsMapper mapper) {
        this.userGoCardlessDetailsRepository = userGoCardlessDetailsRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

@Transactional
public void updateGoCardlessDetailsForLoggedInUser(UserGoCardlessDetailsDto dto) {

    UserGoCardlessDetails existing = userService.findByUsernme(userService.getLoggedInUsername()).map(user -> user.getGoCardlessDetails())
        .orElseThrow(() -> new RuntimeException("User details not found"));

    existing.setAgreementExpDate(dto.getAgreementExpDate());
    existing.setEndUserAgreementId(dto.getEndUserAgreementId());
    existing.setInstitutionId(dto.getInstitutionId());
    existing.setRequisitionId(dto.getRequisitionId());

    userGoCardlessDetailsRepository.save(existing);
}

    public UserGoCardlessDetailsDto getUserGoCardlessDetails() {
    return userService.findByUsernme(userService.getLoggedInUsername())
        .map(u->mapper.toDto(u.getGoCardlessDetails()))
        .orElseThrow(() -> new RuntimeException("User details not found"));
    }
}
