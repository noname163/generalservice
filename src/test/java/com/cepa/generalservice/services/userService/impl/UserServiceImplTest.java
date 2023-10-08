package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.controllers.RedirectController;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;

public class UserServiceImplTest {
    @Mock
    private UserInformationRepository userInformationRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ConfirmTokenService confirmTokenService;

    @Mock
    private RedirectController redirectController;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByEmail() {
        // Mock user information
        UserInformation userInformation = new UserInformation();
        userInformation.setEmail("user@example.com");
        userInformation.setStatus(UserStatus.ENABLE);

        // Mock user repository to return the user information
        when(userInformationRepository.findByEmailAndStatus("user@example.com", UserStatus.ENABLE))
                .thenReturn(Optional.of(userInformation));

        // Call the service method
        UserInformation result = userService.getUserByEmail("user@example.com");

        // Verify the result
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        assertEquals(UserStatus.ENABLE, result.getStatus());
    }

    @Test
    public void testGetUserByEmailNotFound() {
       
        when(userInformationRepository
            .findByEmailAndStatus("nonexistent@example.com", UserStatus.ENABLE))
            .thenReturn(Optional.empty());
        BadRequestException actual = assertThrows( BadRequestException.class ,() -> userService.getUserByEmail("nonexistent@example.com"));
        
        assertEquals("Email nonexistent@example.com is not exist", actual.getMessage());
    }

    @Test
    void testForgotPassword() {

        String uuid = "valid_uuid";
        String password = "new_password";
        String confirmPassword = "new_password";
        String encodePassword = "password_encode";

        UserInformation mockUserInformation = new UserInformation();
        mockUserInformation.setPassword("old_password");

        ForgotPassword forgotPassword = ForgotPassword
                .builder()
                .uuid(uuid)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        when(confirmTokenService.verifyToken(uuid)).thenReturn(true);
        when(confirmTokenService.getUserByToken(uuid)).thenReturn(mockUserInformation);
        when(passwordEncoder.encode(forgotPassword.getPassword())).thenReturn(encodePassword);

        userService.forgotPassword(forgotPassword);

        verify(userInformationRepository, times(1)).save(mockUserInformation);
        verify(passwordEncoder, times(1)).encode(forgotPassword.getPassword());

        assertEquals(mockUserInformation.getPassword(), encodePassword);
    }

    @Test
    void testForgotPasswordWithMismatchedPasswords() {
        // Arrange
        String uuid = "valid_uuid";
        String password = "new_password";
        String confirmPassword = "different_password";
        ForgotPassword forgotPassword = ForgotPassword
                .builder()
                .uuid(uuid)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
        // Act
        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userService.forgotPassword(forgotPassword);
        });

        verify(confirmTokenService, never()).verifyToken(uuid);
        verify(confirmTokenService, never()).getUserByToken(uuid);
        verify(userInformationRepository, never()).save(any());

        assertEquals("Password did not match", actual.getMessage());
    }

    @Test
    void testUserConfirmEmailSuccess() {
        // Arrange
        String token = "38400000-8cf0-11bd-b23e-10b96e4ef00d";

        UserInformation mockUserInformation = new UserInformation();
        mockUserInformation.setEmail("test@example.com");

        ConfirmToken mockUserToken = new ConfirmToken();

        mockUserToken.setToken(UUID.fromString(token));
        when(confirmTokenService.getUserByToken(token)).thenReturn(mockUserInformation);
        when(confirmTokenService.getTokenByEmail(mockUserInformation.getEmail())).thenReturn(mockUserToken);
        when(confirmTokenService.verifyToken(token)).thenReturn(true);

        Boolean result = userService.userConfirmEmail(token);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testUserActivateAccount() {
        // Arrange
        String token = UUID.randomUUID().toString();

        // Mock the behavior of confirmTokenService
        Mockito.when(confirmTokenService.verifyToken(token)).thenReturn(true);
        UserInformation userInformation = new UserInformation();
        Mockito.when(confirmTokenService.getUserByToken(token)).thenReturn(userInformation);

        // Act
        userService.userActivateAccount(token);

        // Assert
        verify(confirmTokenService, times(1)).verifyToken(token);
        verify(confirmTokenService, times(1)).getUserByToken(token);
        verify(userInformationRepository, times(1)).save(userInformation);
    }

}
