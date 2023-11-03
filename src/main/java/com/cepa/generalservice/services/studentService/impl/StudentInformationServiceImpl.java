package com.cepa.generalservice.services.studentService.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
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
            List<StudentTarget> filteredTargets = userInformation.getStudentTargets().stream()
                    .filter(target -> target.getStateType() == StateType.TRUE)
                    .collect(Collectors.toList());
            studentResponse.setTargets(studentTargetMapper.mapEntitiesToDtos(filteredTargets));
            // studentResponse.setTargets(studentTargetMapper
            // .mapEntitiesToDtos(userInformation.getStudentTargets()));
        }
        return studentResponse;
    }

}
