package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponseForAdmin;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseForAdminInterface;

@Component
public class TeacherMapper {
    public TeacherResponse mapEntityToDto(UserInformation userInformation) {
        return TeacherResponse
                .builder()
                .id(userInformation.getId())
                .fullName(userInformation.getFullName())
                .description(userInformation.getDescription())
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

    public TeacherResponseForAdmin mapToTeacherResponseForAdmin(TeacherResponseForAdminInterface teacherResponseForAdminInterface) {
        if (teacherResponseForAdminInterface == null) {
            return null;
        }

        return TeacherResponseForAdmin.builder()
                .id(teacherResponseForAdminInterface.getId())
                .email(teacherResponseForAdminInterface.getEmail())
                .fullName(teacherResponseForAdminInterface.getFullName())
                .url(teacherResponseForAdminInterface.getUrl())
                .createDate(teacherResponseForAdminInterface.getCreateDate())
                .status(teacherResponseForAdminInterface.getStatus())
                .subject(teacherResponseForAdminInterface.getSubject())
                .dateOfBirth(teacherResponseForAdminInterface.getDateOfBirth())
                .indentify(teacherResponseForAdminInterface.getIdentify())
                .build();
    }

    public List<TeacherResponseForAdmin> mapToTeacherResponseForAdminList(List<TeacherResponseForAdminInterface> teacherResponseForAdminInterfaces) {
        return teacherResponseForAdminInterfaces.stream()
                .map(this::mapToTeacherResponseForAdmin)
                .collect(Collectors.toList());
    }

}
