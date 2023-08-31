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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
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
        userInformationRepository = mock(UserInformationRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        loginService = loginService
                .builder()
                .jwtTokenUtil(jwtTokenUtil)
                .passwordEncoder(passwordEncoder)
                .userInformationRepository(userInformationRepository)
                .build();
        loginRequest = loginRequest.builder()
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
        when(userInformationRepository.findByEmail(email)).thenReturn(Optional.of(userInformation));
        when(passwordEncoder.matches(password, userInformation.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateJwtToken(userInformation, 1000)).thenReturn(accessToken);
        when(jwtTokenUtil.generateJwtToken(userInformation, 10000)).thenReturn(refreshToken);

        // Perform the test
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Verify the interactions and assertions
        assertNotNull(loginResponse);
        assertEquals(accessToken, loginResponse.getAccessToken());
        assertEquals(refreshToken, loginResponse.getRefreshToken());

        verify(userInformationRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, userInformation.getPassword());
        verify(jwtTokenUtil).generateJwtToken(userInformation, 1000);
        verify(jwtTokenUtil).generateJwtToken(userInformation, 10000);
    }

    @Test
    void LoginWhenUserNotExistShouldThrowBadRequestException(){

        when(userInformationRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        BadRequestException actual = assertThrows(BadRequestException.class, () -> loginService.login(loginRequest));
        
        assertEquals("User not exist.",actual.getMessage());
    }

    @Test
    void LoginWhenWrongPasswordShouldThrowBadRequestException() {
        String email = "test@gmail.com";
        String password = "password1";

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(email);
        userInformation.setPassword(passwordEncoder.encode(password));


        when(userInformationRepository.findByEmail(email)).thenReturn(Optional.of(userInformation));
        when(passwordEncoder.matches(password, userInformation.getPassword())).thenReturn(true);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> loginService.login(loginRequest));

        assertEquals("Username or password is incorrect. Please try again", actual.getMessage());
    }

}
