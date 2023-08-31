package com.cepa.generalservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cepa.generalservice.GeneralserviceApplication;
import com.cepa.generalservice.configs.SecurityConfig;
import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.userService.RegisterService;
import com.cepa.generalservice.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@ContextConfiguration(classes = { GeneralserviceApplication.class, SecurityConfig.class })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterService registerService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private SecurityContextService securityContextService;

    private UserRegister userRegister;

    @BeforeEach
    void setup() {
        userRegister = UserRegister
                .builder()
                .email("test@gmail.com")
                .password("password")
                .confirmPassword("password")
                .fullName("test12345")
                .role(Role.TEACHER)
                .subjectId(Collections.singletonList(1L))
                .build();
    }

    @Test
    void createAccount() throws Exception {

        doNothing().when(registerService).userRegister(any(UserRegister.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegister)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void loginSuccessReturnLoginResponse() throws JsonProcessingException, Exception {

        LoginRequest loginRequest = LoginRequest
                .builder()
                .email("test@gmail.com")
                .password("password").build();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("sampleAccessToken")
                .refreshToken("sampleRefreshToken").build();

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"accessToken\":\"sampleAccessToken\",\"refreshToken\":\"sampleRefreshToken\"}",
                mvcResult.getResponse().getContentAsString());
    }
}
