package com.example.autobanking.users.mapper;



import org.mapstruct.Mapper;
import com.example.autobanking.users.entity.UserGoCardlessDetails;
import com.example.autobanking.users.model.UserGoCardlessDetailsDto;

@Mapper(componentModel = "spring")
public interface UserGoCardlessDetailsMapper {

    default UserGoCardlessDetails toEntity(UserGoCardlessDetailsDto dto) {
        return UserGoCardlessDetails.builder()
        .agreementExpDate(dto.getAgreementExpDate())
        .endUserAgreementId(dto.getAgreementId())
        .institutionId(dto.getInstitutionId())
        .requisitionId(dto.getRequisitionId()).build(); 
    }
}
