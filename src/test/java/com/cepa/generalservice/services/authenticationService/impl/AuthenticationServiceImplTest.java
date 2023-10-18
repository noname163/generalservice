package com.cepa.generalservice.services.authenticationService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.InValidInformation;
import com.cepa.generalservice.exceptions.SuccessHandler;
import com.cepa.generalservice.exceptions.UserNotExistException;
import com.cepa.generalservice.utils.JwtTokenUtil;

public class AuthenticationServiceImplTest {
    @InjectMocks
    private AuthenticationServiceImpl loginService;

    @Mock
    private UserInformationRepository userInformationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        loginService = AuthenticationServiceImpl
                .builder()
                .jwtTokenUtil(jwtTokenUtil)
                .passwordEncoder(passwordEncoder)
                .userInformationRepository(userInformationRepository)
                .build();
        loginRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("password")
                .build();
    }

    @Test
    void LoginWhenSuccessShouldReturnLoginRespone() {
        // Prepare test data
        String email = "test@gmail.com";
        String password = "password";

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(email);
        userInformation.setPassword(passwordEncoder.encode(password));

        String accessToken = "sampleAccessToken";
        String refreshToken = "sampleRefreshToken";

        // Mock repository behavior
        when(userInformationRepository.findByEmailAndStatus(email, UserStatus.ENABLE))
                .thenReturn(Optional.of(userInformation));
        when(passwordEncoder.matches(password, userInformation.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateJwtToken(userInformation, 1000)).thenReturn(accessToken);
        when(jwtTokenUtil.generateJwtToken(userInformation, 10000)).thenReturn(refreshToken);

        // Perform the test
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Verify the interactions and assertions
        assertNotNull(loginResponse);
        assertEquals(accessToken, loginResponse.getAccessToken());
        assertEquals(refreshToken, loginResponse.getRefreshToken());

        verify(userInformationRepository).findByEmailAndStatus(email, UserStatus.ENABLE);
        verify(passwordEncoder).matches(password, userInformation.getPassword());
        verify(jwtTokenUtil).generateJwtToken(userInformation, 1000);
        verify(jwtTokenUtil).generateJwtToken(userInformation, 10000);
    }

    @Test
    void LoginWhenUserNotExistShouldThrowBadRequestException(){

        when(userInformationRepository.findByEmailAndStatus("test@gmail.com",UserStatus.ENABLE)).thenReturn(Optional.empty());

        UserNotExistException actual = assertThrows(UserNotExistException.class, () -> loginService.login(loginRequest));
        
        assertEquals("User not exist.",actual.getMessage());
    }

    @Test
    void LoginWhenWrongPasswordShouldThrowBadRequestException() {
        String email = "test@gmail.com";
        String password = "password1";

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(email);
        userInformation.setPassword(passwordEncoder.encode(password));

        when(userInformationRepository.findByEmailAndStatus(email, UserStatus.ENABLE))
                .thenReturn(Optional.of(userInformation));
        when(passwordEncoder.matches(password, userInformation.getPassword())).thenReturn(true);

        InValidInformation actual = assertThrows(InValidInformation.class, () -> loginService.login(loginRequest));

        assertEquals("Username or password is incorrect. Please try again", actual.getMessage());
    }

}
