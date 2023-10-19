package com.cepa.generalservice.services.authenticationService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.cepa.generalservice.exceptions.UserNotExistException;
import com.cepa.generalservice.utils.JwtTokenUtil;

class AuthenticationServiceImplTest {
    @Mock
    private UserInformationRepository userInformationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        // Create a sample LoginRequest and UserInformation
        LoginRequest loginRequest = LoginRequest.builder().build();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail("test@example.com");
        userInformation.setPassword("hashed_password");

        // Mock the behavior of the userInformationRepository
        when(userInformationRepository.findByEmailAndStatus("test@example.com", UserStatus.ENABLE))
                .thenReturn(java.util.Optional.of(userInformation));

        when(passwordEncoder.matches("password", "hashed_password")).thenReturn(true);
        when(jwtTokenUtil.generateJwtToken(userInformation, 1000)).thenReturn("test_access_token");
        when(jwtTokenUtil.generateJwtToken(userInformation, 10000)).thenReturn("test_refresh_token");
        LoginResponse loginResponse = authenticationService.login(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("test_access_token", loginResponse.getAccessToken());
        assertEquals("test_refresh_token", loginResponse.getRefreshToken());
    }

    @Test()
    void testLogin_UserNotExist() {
        // Create a sample LoginRequest
        LoginRequest loginRequest = LoginRequest.builder().build();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(userInformationRepository.findByEmailAndStatus("test@example.com", UserStatus.ENABLE))
                .thenReturn(java.util.Optional.empty());

        UserNotExistException result =  assertThrows(UserNotExistException.class, () -> authenticationService.login(loginRequest));

        assertEquals("User not exist.", result.getMessage());
    }

}
