package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.UserRegister;
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

    private UserRegister teacherRegiter;

    private UserInformation existUSer;

    @BeforeEach
    void setup() {
        teacherRegiter = UserRegister
                .builder()
                .email("test@example.com")
                .password("password")
                .confirmPassword("password")
                .fullName("test")
                .role(Role.TEACHER)
                .subjectId(Collections.singletonList(1L))
                .build();

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void teacherRegiterWhenEmailExistReturnBadRequestException(){

        when(userInformationRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existUSer));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> registerService.userRegister(teacherRegiter));
        
        assertEquals("Email test@example.com is already exist", actual.getMessage());
    }

    @Test
    void teacherRegiterWhenPasswordNotMatchReturnBadRequestException(){

        when(userInformationRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        teacherRegiter.setConfirmPassword("something");

        BadRequestException actual = assertThrows(BadRequestException.class, () -> registerService.userRegister(teacherRegiter));
        
        assertEquals("Password did not match.", actual.getMessage());
    }

    // @Test
    // void userConfirmEmail_ShouldEnableUserStatus_WhenTokenIsValid() {

    //     UUID token = UUID.randomUUID();
    //     String tokenStr = token.toString();
    //     UserInformation userInformation = createUserInformation(UserStatus.WATTING);

    //     ConfirmToken userToken = ConfirmToken.builder().token(token).build();
    //     userToken.setToken(token);

    //     when(confirmTokenService.getUserByToken(tokenStr)).thenReturn(userInformation);
    //     when(confirmTokenService.getTokenByEmail(userInformation.getEmail())).thenReturn(userToken);
    //     when(confirmTokenService.verifyToken(tokenStr)).thenReturn(true);

    //     registerService.userConfirmEmail(tokenStr);

    //     verify(confirmTokenService, times(1)).getUserByToken(tokenStr);
    //     verify(confirmTokenService, times(1)).getTokenByEmail(userInformation.getEmail());
    //     verify(confirmTokenService, times(1)).verifyToken(tokenStr);

    //     assert userInformation.getStatus() == UserStatus.ENABLE;
    //     verify(userInformationRepository, times(1)).save(userInformation);
    // }

    // @Test
    // void userConfirmEmail_ShouldThrowBadRequestException_WhenTokenIsNotValid() {
    //     // Arrange
    //     UUID token = UUID.randomUUID();
    //     String tokenStr = "invalid-token";
    //     UserInformation userInformation = createUserInformation(UserStatus.WATTING);

    //     ConfirmToken userToken = ConfirmToken.builder().token(token).build();
    //     userToken.setToken(token);

    //     when(confirmTokenService.getUserByToken(tokenStr)).thenReturn(userInformation);
    //     when(confirmTokenService.getTokenByEmail(userInformation.getEmail())).thenReturn(userToken);

    //     assertThrows(BadRequestException.class, () -> registerService.userConfirmEmail(tokenStr));

    //     assert userInformation.getStatus() == UserStatus.WATTING;
    //     verify(userInformationRepository, never()).save(userInformation);
    // }

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
