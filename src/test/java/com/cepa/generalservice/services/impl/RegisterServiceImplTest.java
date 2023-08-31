package com.cepa.generalservice.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.cepa.generalservice.services.studentService.StudentTargetService;
import com.cepa.generalservice.services.userService.RegisterService;
import com.cepa.generalservice.services.userService.impl.RegisterServiceImpl;

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
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInformationMapper userInformationMapper;

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

        registerService = RegisterServiceImpl
                .builder()
                .passwordEncoder(passwordEncoder)
                .studentTargetService(studentTargetService)
                .subjectRepository(subjectRepository)
                .teacherRepository(teacherRepository)
                .userInformationMapper(userInformationMapper)
                .userInformationRepository(userInformationRepository)
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

        when(userInformationRepository.findByEmail(userRegister.getEmail())).thenReturn(Optional.empty());
        when(userInformationMapper.mapDtoToEntity(userRegister)).thenReturn(userInformation);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userInformationRepository.save(userInformation)).thenReturn(userInformation);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(new Subject()));
        when(teacherRepository.findByInformationId(0L)).thenReturn(Optional.empty());

        registerService.userRegister(userRegister);

        verify(userInformationRepository).findByEmail(userRegister.getEmail());
        verify(userInformationMapper).mapDtoToEntity(userRegister);
        verify(userInformation).setStatus(UserStatus.ENABLE);
        verify(passwordEncoder).encode("password");
        verify(teacherRepository).findByInformationId(0L);
        verify(subjectRepository).findById(1L);
        verify(teacherRepository).save(any(Teacher.class));
    }

}
