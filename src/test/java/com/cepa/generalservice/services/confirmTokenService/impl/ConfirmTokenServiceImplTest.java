package com.cepa.generalservice.services.confirmTokenService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.cepa.generalservice.data.constants.TokenStatus;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.ConfirmTokenRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;

public class ConfirmTokenServiceImplTest {
    @InjectMocks
    private ConfirmTokenServiceImpl confirmTokenService;

    @Mock
    private ConfirmTokenRepository confirmTokenRepository;

    @Mock
    private UserInformationRepository userInformationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveConfirmTokenSuccess() {
        // Arrange
        String email = "test@example.com";
        UserInformation mockUserInformation = new UserInformation();
        mockUserInformation.setEmail(email);

        when(userInformationRepository.findByEmail(email)).thenReturn(Optional.of(mockUserInformation));
        when(confirmTokenRepository.findByUserInformation(mockUserInformation)).thenReturn(Optional.empty());

        // Act
        UUID token = confirmTokenService.saveConfirmToken(email);

        // Assert
        assertNotNull(token);
        verify(confirmTokenRepository, times(1)).save(any(ConfirmToken.class));
    }

    @Test
    void testSaveConfirmTokenTokenExists() {
        // Arrange
        String email = "test@example.com";
        UserInformation mockUserInformation = new UserInformation();
        mockUserInformation.setEmail(email);

        ConfirmToken existingToken = new ConfirmToken();
        existingToken.setToken(UUID.randomUUID());
        existingToken.setCount(0);

        when(userInformationRepository.findByEmail(email)).thenReturn(Optional.of(mockUserInformation));
        when(confirmTokenRepository.findByUserInformation(mockUserInformation)).thenReturn(Optional.of(existingToken));

        // Act
        UUID token = confirmTokenService.saveConfirmToken(email);

        // Assert
        assertNotNull(token);
        verify(confirmTokenRepository, times(1)).save(any(ConfirmToken.class));
    }

    @Test
    void testSaveConfirmTokenUserNotFound() {
        // Arrange
        String email = "test@example.com";
        when(userInformationRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.saveConfirmToken(email);
        });

        verify(confirmTokenRepository, never()).save(any(ConfirmToken.class));
        assertEquals("User with email not found: test@example.com", actual.getMessage());
    }

    @Test
    void testVerifyTokenSuccess() {
        // Arrange
        UUID providedToken = UUID.randomUUID();
        ConfirmToken mockConfirmToken = new ConfirmToken();
        mockConfirmToken.setToken(providedToken);
        mockConfirmToken.setStatus(TokenStatus.CREATED);
        LocalDateTime now = LocalDateTime.now();
        mockConfirmToken.setCreateAt(now.minusMinutes(10)); // Token is not expired
        mockConfirmToken.setExpriedAt(mockConfirmToken.getCreateAt().plusMinutes(5));

        when(confirmTokenRepository.findByTokenAndStatusNot(providedToken,TokenStatus.CHANGED))
                .thenReturn(Optional.of(mockConfirmToken));

        // Act
        boolean result = confirmTokenService.verifyToken(providedToken.toString());

        // Assert
        assertTrue(result);
        assertEquals(TokenStatus.CONFIRMED, mockConfirmToken.getStatus());
        assertEquals(0, mockConfirmToken.getCount());
        verify(confirmTokenRepository, times(1)).save(mockConfirmToken);
    }

    @Test
    void testVerifyTokenTokenExpired() {
        // Arrange
        UUID providedToken = UUID.randomUUID();
        ConfirmToken mockConfirmToken = new ConfirmToken();
        mockConfirmToken.setToken(providedToken);
        mockConfirmToken.setStatus(TokenStatus.CONFIRMED);;
        LocalDateTime now = LocalDateTime.now();
        mockConfirmToken.setCreateAt(now); // Token is not expired
        mockConfirmToken.setExpriedAt(mockConfirmToken.getCreateAt().minusMinutes(5));

        when(confirmTokenRepository.findByTokenAndStatusNot(providedToken,TokenStatus.CHANGED))
                .thenReturn(Optional.of(mockConfirmToken));

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.verifyToken(providedToken.toString());
        });

        assertEquals("Token has expired.", actual.getMessage());
        verify(confirmTokenRepository, never()).save(mockConfirmToken);
    }

    @Test
    void testGetUserByTokenSuccess() {
        // Arrange
        UUID providedToken = UUID.randomUUID();
        UserInformation mockUserInformation = new UserInformation();
        ConfirmToken mockConfirmToken = new ConfirmToken();
        mockConfirmToken.setToken(providedToken);
        mockConfirmToken.setStatus(TokenStatus.CREATED);
        mockConfirmToken.setUserInformation(mockUserInformation);

        when(confirmTokenRepository.findByToken(providedToken))
                .thenReturn(Optional.of(mockConfirmToken));

        // Act
        UserInformation result = confirmTokenService.getUserByToken(providedToken.toString());

        // Assert
        assertEquals(mockUserInformation, result);
    }

    @Test
    void testGetUserByTokenTokenNotFound() {
        // Arrange
        UUID providedToken = UUID.randomUUID();

        when(confirmTokenRepository.findByTokenAndStatusNot(providedToken,TokenStatus.CHANGED)).thenReturn(Optional.empty());

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.getUserByToken(providedToken.toString());
        });
        assertEquals("Token not valid.", actual.getMessage());
    }

    @Test
    void testReCreateTokenSuccess() {
        // Arrange
        ConfirmToken mockConfirmToken = new ConfirmToken();
        mockConfirmToken.setCount(4); // Less than 5
        LocalDateTime now = LocalDateTime.now();
        mockConfirmToken.setCreateAt(now.minusSeconds(31));
        mockConfirmToken.setExpriedAt(now.plusMinutes(1));
        when(confirmTokenRepository.save(any())).thenReturn(mockConfirmToken);

        // Act
        UUID result = confirmTokenService.reCreateToken(mockConfirmToken);

        // Assert
        assertNotNull(result);
        verify(confirmTokenRepository, times(1)).save(mockConfirmToken);
    }

    @Test
    void testReCreateTokenCountExceeded() {
        // Arrange
        ConfirmToken mockConfirmToken = new ConfirmToken();
        mockConfirmToken.setCount(6);
        LocalDateTime now = LocalDateTime.now();
        mockConfirmToken.setCreateAt(now.plusNanos(10));
        mockConfirmToken.setExpriedAt(now.plusMinutes(1));

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.reCreateToken(mockConfirmToken);
        });

        verify(confirmTokenRepository, never()).save(mockConfirmToken);
        assertEquals("Please try again after 2 minus", actual.getMessage());
    }

    @Test
    void testGetTokenByEmailSuccess() {
        // Arrange
        String userEmail = "test@example.com";
        UserInformation mockUserInformation = new UserInformation();
        when(userInformationRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUserInformation));
        ConfirmToken mockConfirmToken = new ConfirmToken();
        when(confirmTokenRepository.findByUserInformation(mockUserInformation))
                .thenReturn(Optional.of(mockConfirmToken));

        // Act
        ConfirmToken result = confirmTokenService.getTokenByEmail(userEmail);

        // Assert
        assertEquals(mockConfirmToken, result);
    }

    @Test
    void testGetTokenByEmailUserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(userInformationRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.getTokenByEmail(userEmail);
        });
        assertEquals("Not exist user with email test@example.com", actual.getMessage());
    }

    @Test
    void testGetTokenByEmailTokenNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        UserInformation mockUserInformation = new UserInformation();
        when(userInformationRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUserInformation));
        when(confirmTokenRepository.findByUserInformation(mockUserInformation)).thenReturn(Optional.empty());

        // Act and Assert
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            confirmTokenService.getTokenByEmail(userEmail);
        });

        assertEquals("This user not have token.", actual.getMessage());
    }

}
