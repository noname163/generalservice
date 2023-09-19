package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
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

    private UserRegister userRegister;

    @BeforeEach
    void setup() {

        userInformationRepository = mock(UserInformationRepository.class);
        subjectRepository = mock(SubjectRepository.class);
        teacherRepository = mock(TeacherRepository.class);
        studentTargetService = mock(StudentTargetService.class);
        userInformationMapper = mock(UserInformationMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        confirmTokenService = mock(ConfirmTokenService.class);
        sendEmailService = mock(SendEmailService.class);
        userRegister = mock(UserRegister.class);

        registerService = RegisterServiceImpl
                .builder()
                .passwordEncoder(passwordEncoder)
                .studentTargetService(studentTargetService)
                .subjectRepository(subjectRepository)
                .teacherRepository(teacherRepository)
                .userInformationMapper(userInformationMapper)
                .userInformationRepository(userInformationRepository)
                .confirmTokenService(confirmTokenService)
                .build();
    }

    @Test
    void teacherRegister_ShouldRegisterTeacher() {
        // Arrange
        TeacherRegister teacherRegister = createTeacherRegister();

        UserInformation newUser = createUserInformation();
        when(userInformationRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(subjectRepository.findById(any())).thenReturn(Optional.of(new Subject()));
        when(userInformationMapper.mapDtoToEntity(teacherRegister.getUserRegister())).thenReturn(newUser);
        when(userInformationRepository.save(any())).thenReturn(newUser);

        // Act
        registerService.teacherRegister(teacherRegister);

        // Assert
        verify(userInformationRepository, times(1)).findByEmail(any());
        verify(subjectRepository, times(1)).findById(any());
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
        BadRequestException actual = assertThrows(BadRequestException.class,
                () -> registerService.teacherRegister(teacherRegister));

        assertEquals("Email teacher@example.com is already exist", actual.getMessage());
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

        BadRequestException actual = assertThrows(BadRequestException.class,
                () -> registerService.studentRegister(studentRegister));

        assertEquals("Email student@example.com is already exist", actual.getMessage());
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

        BadRequestException actual = assertThrows(BadRequestException.class,
                () -> registerService.studentRegister(studentRegister));

        assertEquals("Password did not match.", actual.getMessage());
    }

    @Test
    void userConfirmEmail_ShouldEnableUserStatus_WhenTokenIsValid() {

        UUID token = UUID.randomUUID();
        String tokenStr = token.toString();
        UserInformation userInformation = createUserInformation(UserStatus.WATTING);

        ConfirmToken userToken = ConfirmToken.builder().token(token).build();
        userToken.setToken(token);

        when(confirmTokenService.getUserByToken(tokenStr)).thenReturn(userInformation);
        when(confirmTokenService.getTokenByEmail(userInformation.getEmail())).thenReturn(userToken);
        when(confirmTokenService.verifyToken(tokenStr)).thenReturn(true);

        registerService.userConfirmEmail(tokenStr);

        verify(confirmTokenService, times(1)).getUserByToken(tokenStr);
        verify(confirmTokenService, times(1)).getTokenByEmail(userInformation.getEmail());
        verify(confirmTokenService, times(1)).verifyToken(tokenStr);

        assert userInformation.getStatus() == UserStatus.ENABLE;
        verify(userInformationRepository, times(1)).save(userInformation);
    }

    @Test
    void userConfirmEmail_ShouldThrowBadRequestException_WhenTokenIsNotValid() {
        // Arrange
        UUID token = UUID.randomUUID();
        String tokenStr = "invalid-token";
        UserInformation userInformation = createUserInformation(UserStatus.WATTING);

        ConfirmToken userToken = ConfirmToken.builder().token(token).build();
        userToken.setToken(token);

        when(confirmTokenService.getUserByToken(tokenStr)).thenReturn(userInformation);
        when(confirmTokenService.getTokenByEmail(userInformation.getEmail())).thenReturn(userToken);

        assertThrows(BadRequestException.class, () -> registerService.userConfirmEmail(tokenStr));

        assert userInformation.getStatus() == UserStatus.WATTING;
        verify(userInformationRepository, never()).save(userInformation);
    }

    private UserInformation createUserInformation(UserStatus status) {
        UserInformation userInformation = new UserInformation();
        userInformation.setStatus(status);
        return userInformation;
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
                .subjectId(1l)
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
