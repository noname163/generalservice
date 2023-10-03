package com.cepa.generalservice.services.studentService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.mappers.StudentMapper;
import com.cepa.generalservice.mappers.StudentTargetMapper;
import com.cepa.generalservice.services.studentService.StudentInformationService;
import com.cepa.generalservice.services.userService.UserService;

import lombok.Builder;

@Service
@Builder
public class StudentInformationServiceImpl implements StudentInformationService {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentTargetMapper studentTargetMapper;

    @Override
    public StudentResponse getStudentByEmail(String email) {
        UserInformation userInformation = userService.getUserByEmail(email);
        StudentResponse studentResponse = studentMapper.mapEntityToDto(userInformation);
        if (userInformation.getStudentTargets() != null
                && !userInformation.getStudentTargets().isEmpty()) {
            studentResponse.setTargets(studentTargetMapper
                    .mapEntitiesToDtos(userInformation.getStudentTargets()));
        }
        return studentResponse;
    }

}
