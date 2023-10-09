package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.SuccessHandler;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.studentService.StudentTargetService;

public class RegisterServiceImplTest {
    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private UserInformationRepository userInformationRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentTargetService studentTargetService;

    @Mock
    private SendEmailService sendEmailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInformationMapper userInformationMapper;

    @Mock
    private ConfirmTokenService confirmTokenService;


    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void teacherRegister_ShouldRegisterTeacher() {
        // Arrange
        TeacherRegister teacherRegister = createTeacherRegister();

        UserInformation newUser = createUserInformation();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(subjectRepository.findByIdIn(any())).thenReturn(Optional.of(List.of(new Subject())));
        when(userInformationMapper.mapDtoToEntity(teacherRegister.getUserRegister())).thenReturn(newUser);
        when(userInformationRepository.save(any())).thenReturn(newUser);

        // Act
        registerService.teacherRegister(teacherRegister);

        // Assert
        verify(userInformationRepository, times(1)).findByEmail(any());
        verify(userInformationRepository, times(1)).save(any());
        verify(userInformationMapper, times(1)).mapDtoToEntity(teacherRegister.getUserRegister());
        verify(teacherRepository, times(1)).save(any());
    }

    @Test
    void teacherRegister_ShouldThrowBadRequestException_WhenEmailExists() {
        // Arrange
        TeacherRegister teacherRegister = createTeacherRegister();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.of(new UserInformation()));

        // Act & Assert
        SuccessHandler actual = assertThrows(SuccessHandler.class,
                () -> registerService.teacherRegister(teacherRegister));

        assertEquals("1", actual.getMessage());
    }

    @Test
    void studentRegister_ShouldRegisterStudent() {
        // Arrange
        StudentRegister studentRegister = createStudentRegister();

        UserInformation newUser = createUserInformation();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userInformationMapper.mapDtoToEntity(studentRegister.getUserRegister())).thenReturn(newUser);
        when(userInformationRepository.save(any())).thenReturn(newUser);

        // Act
        registerService.studentRegister(studentRegister);

        // Assert
        verify(userInformationRepository, times(1)).findByEmail(any());
        verify(userInformationMapper).mapDtoToEntity(studentRegister.getUserRegister());
        verify(userInformationRepository, times(1)).save(any());
        verify(studentTargetService, times(1)).createStudentTarget(any(), anyList());
    }

    @Test
    void studentRegister_ShouldThrowBadRequestException_WhenEmailExists() {

        StudentRegister studentRegister = createStudentRegister();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.of(new UserInformation()));

        SuccessHandler actual = assertThrows(SuccessHandler.class,
                () -> registerService.studentRegister(studentRegister));

        assertEquals("1", actual.getMessage());
    }

    @Test
    void userRegister_ShouldThrowBadRequestException_WhenPasswordDidNotMatch() {
        UserRegister userRegister = UserRegister
                .builder()
                .email("student@example.com")
                .confirmPassword("123456")
                .password("12345")
                .build();

        StudentRegister studentRegister = StudentRegister.builder().userRegister(userRegister).build();
        UserInformation newUser = createUserInformation();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userInformationMapper.mapDtoToEntity(studentRegister.getUserRegister())).thenReturn(newUser);
        when(userInformationRepository.save(any())).thenReturn(newUser);

        SuccessHandler actual = assertThrows(SuccessHandler.class,
                () -> registerService.studentRegister(studentRegister));

        assertEquals("2", actual.getMessage());
    }


    private TeacherRegister createTeacherRegister() {
        UserRegister userRegister = UserRegister
                .builder()
                .email("teacher@example.com")
                .confirmPassword("123456")
                .password("123456")
                .build();
        userRegister.setEmail("teacher@example.com");
        return TeacherRegister
                .builder()
                .userRegister(userRegister)
                .subjectIds(List.of(1l))
                .build();
    }

    private StudentRegister createStudentRegister() {
        UserRegister userRegister = UserRegister
                .builder()
                .email("student@example.com")
                .confirmPassword("123456")
                .password("123456")
                .build();

        return StudentRegister
                .builder()
                .userRegister(userRegister)
                .combinationIds(List.of(1L, 2L)).build();
    }

    private UserInformation createUserInformation() {
        UserInformation userInformation = UserInformation.builder().build();
        return userInformation;
    }

}
