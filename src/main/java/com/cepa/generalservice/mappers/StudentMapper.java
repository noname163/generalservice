package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;

@Component
public class StudentMapper {
    public StudentResponse mapEntityToDto(UserInformation userInformation) {
        return StudentResponse
                .builder()
                .id(userInformation.getId())
                .description(userInformation.getDescription())
                .fullName(userInformation.getFullName())
                .userStatus(userInformation.getStatus())
                .email(userInformation.getEmail())
                .url(userInformation.getImageURL() != null ? userInformation.getImageURL() : "empty")
                .dateOfBirth(userInformation.getDateOfBirth() != null ? userInformation.getDateOfBirth() : null)
                .build();
    }

    public List<StudentResponse> mapEntitiesToDtos(List<UserInformation> students) {
        return students.stream().map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
