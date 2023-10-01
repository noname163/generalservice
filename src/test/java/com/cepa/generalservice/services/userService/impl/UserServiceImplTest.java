package com.cepa.generalservice.services.userService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;

public class UserServiceImplTest {
    @Mock
    private UserInformationRepository userInformationRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
}
