package com.cepa.generalservice.services.studentService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.mappers.StudentMapper;
import com.cepa.generalservice.mappers.StudentTargetMapper;
import com.cepa.generalservice.services.userService.UserService;

public class StudentInformationServiceImplTest {
    
    @Mock
    private UserService userService;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentTargetMapper studentTargetMapper;

    @InjectMocks
    private StudentInformationServiceImpl studentInformationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getStudentByEmail_WhenTargetEmpty() {

        List<StudentTarget> studentTargets = Collections.emptyList();

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail("student@example.com");
        userInformation.setStatus(UserStatus.ENABLE);
        userInformation.setStudentTargets(studentTargets);

        StudentResponse studentResponse = StudentResponse.builder().build();
        studentResponse.setEmail("student@example.com");

        when(userService.getUserByEmail("student@example.com")).thenReturn(userInformation);
       
        when(studentMapper.mapEntityToDto(userInformation)).thenReturn(studentResponse);

        StudentResponse result = studentInformationService.getStudentByEmail("student@example.com");

        // Verify the result
        assertEquals(studentResponse, result);
        assertEquals(null, result.getTargets());
    }

    @Test
    public void getStudentByEmail_WhenTargetNotEmpty() {

        List<StudentTarget> studentTargets = List.of(new StudentTarget());
        List<StudentTargetResponse> studentTargetResponses = List.of(StudentTargetResponse.builder().build());

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail("student@example.com");
        userInformation.setStatus(UserStatus.ENABLE);
        userInformation.setStudentTargets(studentTargets);

        StudentResponse studentResponse = StudentResponse.builder().build();
        studentResponse.setEmail("student@example.com");

        when(userService.getUserByEmail("student@example.com")).thenReturn(userInformation);
        when(studentTargetMapper.mapEntitiesToDtos(studentTargets)).thenReturn(studentTargetResponses);
        when(studentMapper.mapEntityToDto(userInformation)).thenReturn(studentResponse);

        StudentResponse result = studentInformationService.getStudentByEmail("student@example.com");

        verify(studentTargetMapper).mapEntitiesToDtos(studentTargets);

        assertEquals(studentResponse, result);
        assertEquals(studentTargetResponses, result.getTargets());
    }
}
