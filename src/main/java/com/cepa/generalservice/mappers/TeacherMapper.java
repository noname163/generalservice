package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.entities.UserInformation;

@Component
public class TeacherMapper {
    public TeacherResponse mapEntityToDto(UserInformation userInformation) {
        return TeacherResponse
                .builder()
                .id(userInformation.getId())
                .fullName(userInformation.getFullName())
                .createDate(userInformation.getCreateDate())
                .status(userInformation.getStatus())
                .email(userInformation.getEmail())
                .url(userInformation.getImageURL() != null ? userInformation.getImageURL() : "empty")
                .dateOfBirth(userInformation.getDateOfBirth() != null ? userInformation.getDateOfBirth() : null)
                .build();
    }

    public List<TeacherResponse> mapEntitiesToDtos(List<UserInformation> teachers) {
        return teachers.stream().map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

}
