package com.cepa.generalservice.mappers;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.entities.UserInformation;

@Component
public class StudentMapper {
    public StudentResponse mapEntityToDto(UserInformation userInformation) {
        return StudentResponse
                .builder()
                .id(userInformation.getId())
                .fullName(userInformation.getFullName())
                .email(userInformation.getEmail())
                .url(userInformation.getImageURL() != null ? userInformation.getImageURL() : "empty")
                .dateOfBirth(userInformation.getDateOfBirth() != null ? userInformation.getDateOfBirth() : null)
                .build();
    }
}
