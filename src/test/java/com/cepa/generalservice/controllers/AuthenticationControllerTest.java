package com.cepa.generalservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cepa.generalservice.GeneralserviceApplication;
import com.cepa.generalservice.configs.SecurityConfig;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.event.EventPublisher;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.userService.RegisterService;
import com.cepa.generalservice.services.userService.UserService;
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

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    public void testCreateTeacherAccount() throws Exception {

        doNothing().when(registerService).teacherRegister(any(TeacherRegister.class));

        String requestBody = "{ \"userRegister\": { \"email\": \"teacher@example.com\", \"fullName\": \"John Doe\" }, \"subjectIds\": [1,2,3]}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/authentication/register/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateStudentAccount() throws Exception {

        doNothing().when(registerService).studentRegister(any(StudentRegister.class));

        String requestBody = "{ \"userRegister\": { \"email\": \"student@example.com\", \"fullName\": \"John Doe\" }, \"combinationIds\": [1,2] }";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/authentication/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
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

    @Test
    public void testConfirmOtpWhenSuccessReturnOK() throws Exception {

        String token = "sampleToken";

        doNothing().when(registerService).userConfirmEmail(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authentication/confirm")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(registerService).userConfirmEmail(token);
    }

    @Test
    public void testConfirmOtpWhenSuccessReturnFail() throws Exception {

        Mockito.doThrow(new BadRequestException("Token not valid"))
                .when(registerService)
                .userConfirmEmail(any());

        // Mock the request with an invalid token
        String invalidToken = "invalid-token";

        ResultActions actual = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/authentication/confirm")
                .param("token", invalidToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertEquals("{\"message\":\"Token not valid\"}",
                actual.andReturn().getResponse().getContentAsString());
    }

}
