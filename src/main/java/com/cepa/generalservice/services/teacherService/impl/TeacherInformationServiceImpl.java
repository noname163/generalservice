package com.cepa.generalservice.services.teacherService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.mappers.StudentMapper;
import com.cepa.generalservice.mappers.StudentTargetMapper;
import com.cepa.generalservice.mappers.TeacherMapper;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;
import com.cepa.generalservice.services.userService.UserService;

import lombok.Builder;

@Service
@Builder
public class TeacherInformationServiceImpl implements TeacherInformationService {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public TeacherResponse getTeacherByEmail(String email) {
        UserInformation userInformation = userService.getUserByEmail(email);
        TeacherResponse teacherResponse = teacherMapper.mapEntityToDto(userInformation);
        return teacherResponse;
    }

}
