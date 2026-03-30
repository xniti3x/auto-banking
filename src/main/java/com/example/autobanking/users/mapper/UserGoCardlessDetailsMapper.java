package com.example.autobanking.users.mapper;



import org.mapstruct.Mapper;

import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.model.UserGoCardlessDetailsDto;

@Mapper(componentModel = "spring")
public interface UserGoCardlessDetailsMapper {

    UserGoCardlessDetailsDto toDto(UserGoCardlessDetails entity);
    UserGoCardlessDetails toEntity(UserGoCardlessDetailsDto dto);
}
