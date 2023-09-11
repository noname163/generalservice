package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.valueOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.mail.SendFailedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.UserStatus;
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

    private UserInformation existUSer;

    @BeforeEach
    void setup() {
        userRegister = UserRegister
                .builder()
                .email("test@example.com")
                .password("password")
                .confirmPassword("password")
                .fullName("test")
                .role(Role.TEACHER)
                .subjectId(Collections.singletonList(1L))
                .build();

        userInformationRepository = mock(UserInformationRepository.class);
        subjectRepository = mock(SubjectRepository.class);
        teacherRepository = mock(TeacherRepository.class);
        studentTargetService = mock(StudentTargetService.class);
        userInformationMapper = mock(UserInformationMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        existUSer = mock(UserInformation.class);
        confirmTokenService = mock(ConfirmTokenService.class);
        sendEmailService = mock(SendEmailService.class);

        registerService = RegisterServiceImpl
                .builder()
                .passwordEncoder(passwordEncoder)
                .studentTargetService(studentTargetService)
                .subjectRepository(subjectRepository)
                .teacherRepository(teacherRepository)
                .userInformationMapper(userInformationMapper)
                .userInformationRepository(userInformationRepository)
                .confirmTokenService(confirmTokenService)
                .sendEmailService(sendEmailService)
                .build();
    }

    @Test
    void userRegisterWhenEmailExistReturnBadRequestException(){

        when(userInformationRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existUSer));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> registerService.userRegister(userRegister));
        
        assertEquals("Email test@example.com is already exist", actual.getMessage());
    }

    @Test
    void userRegisterWhenPasswordNotMatchReturnBadRequestException(){

        when(userInformationRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        userRegister.setConfirmPassword("something");

        BadRequestException actual = assertThrows(BadRequestException.class, () -> registerService.userRegister(userRegister));
        
        assertEquals("Password did not match.", actual.getMessage());
    }

    @Test
    void userRegisterTeacherRoleWhenSuccessReturnVoid() {

        UserInformation userInformation = mock(UserInformation.class);
        UUID uuid = UUID.randomUUID();

        when(userInformationRepository.findByEmail(userRegister.getEmail())).thenReturn(Optional.empty());
        when(userInformationMapper.mapDtoToEntity(userRegister)).thenReturn(userInformation);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userInformationRepository.save(userInformation)).thenReturn(userInformation);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(new Subject()));
        when(teacherRepository.findByInformationId(0L)).thenReturn(Optional.empty());
        when(confirmTokenService.saveConfirmToken(userInformation)).thenReturn(uuid);

        registerService.userRegister(userRegister);

        verify(userInformationRepository).findByEmail(userRegister.getEmail());
        verify(userInformationMapper).mapDtoToEntity(userRegister);
        verify(userInformation).setStatus(UserStatus.WATTING);
        verify(passwordEncoder).encode("password");
        verify(teacherRepository).findByInformationId(0L);
        verify(subjectRepository).findById(1L);
        verify(teacherRepository).save(any(Teacher.class));
        verify(confirmTokenService).saveConfirmToken(userInformation);

        String url = "http://localhost:8080/api/authentication/confirm?token=" + uuid.toString();
        try {
            verify(sendEmailService)
                    .sendVerificationEmail(userInformation.getEmail(),
                            userInformation.getFullName(),
                            url);
        } catch (SendFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
