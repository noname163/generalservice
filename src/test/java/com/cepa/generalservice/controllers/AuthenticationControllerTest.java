package com.cepa.generalservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cepa.generalservice.GeneralserviceApplication;
import com.cepa.generalservice.configs.SecurityConfig;
import com.cepa.generalservice.data.dto.request.EmailRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.event.EventPublisher;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.userService.RegisterService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.EnvironmentVariables;
import com.cepa.generalservice.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@ContextConfiguration(classes = { GeneralserviceApplication.class, SecurityConfig.class })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthenticationControllerTest {
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
        private UserService userService;

        @MockBean
        private EventPublisher eventPublisher;

        @MockBean
        private EnvironmentVariables environmentVariables;

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Test
        void testCreateTeacherAccount() throws Exception {

                doNothing().when(registerService).teacherRegister(any(TeacherRegister.class));

                UserRegister userRegister = UserRegister
                                .builder()
                                .email("teacher@gmail.com")
                                .confirmPassword("123456")
                                .fullName("test12345")
                                .password("123456")
                                .build();
                TeacherRegister teacherRegister = TeacherRegister
                                .builder()
                                .userRegister(userRegister)
                                .subjectIds(List.of(1l, 2l, 3l))
                                .build();

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/authentication/register/teacher")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(teacherRegister)))
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        void testCreateStudentAccount() throws Exception {

                doNothing().when(registerService).studentRegister(any(StudentRegister.class));

                UserRegister userRegister = UserRegister
                                .builder()
                                .email("student@gmail.com")
                                .confirmPassword("123456")
                                .fullName("test12345")
                                .password("123456")
                                .build();
                StudentRegister studentRegister = StudentRegister
                                .builder()
                                .userRegister(userRegister)
                                .combinationIds(List.of(1l, 2l, 3l))
                                .build();

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/authentication/register/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentRegister)))
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
        void testForgotPasswordSuccess() throws Exception {

                String userEmail = "test@gmail.com";
                UserInformation userInformation = UserInformation.builder().build();
                EmailRequest emailRequest = EmailRequest.builder().email(userEmail).build();
                when(userService.getUserByEmail(userEmail)).thenReturn(userInformation);

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/forgot-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(emailRequest)))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testForgotPasswordUserNotFound() throws Exception {

                String userEmail = "nonexistent@gmail.com";
                EmailRequest emailRequest = EmailRequest.builder().email(userEmail).build();
                when(userService.getUserByEmail(emailRequest.getEmail()))
                                .thenThrow(new BadRequestException("User not valid."));

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/forgot-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(emailRequest)))
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User not valid.\"}"))
                                .andReturn();
        }

        @Test
        void testResetPasswordSuccess() throws Exception {

                ForgotPassword forgotPassword = ForgotPassword.builder().build();
                forgotPassword.setPassword("newPassword");
                forgotPassword.setConfirmPassword("newPassword");
                forgotPassword.setUuid("token123");

                doNothing().when(userService).forgotPassword(any(ForgotPassword.class));

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"password\":\"newPassword\",\"confirmPassword\":\"newPassword\",\"uuid\":\"token123\"}"))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testResetPasswordUserNotValid() throws Exception {
                ForgotPassword forgotPassword = ForgotPassword.builder().build();
                forgotPassword.setPassword("newPassword");
                forgotPassword.setConfirmPassword("newPassword");
                forgotPassword.setUuid("token123");

                Mockito.doThrow(new BadRequestException("User not valid.")).when(userService)
                                .forgotPassword(any(ForgotPassword.class));

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(forgotPassword)))
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User not valid.\"}"))
                                .andReturn();
        }

    @Test
    void testConfirmOtpSuccess() throws Exception {

        when(userService.userConfirmEmail(Mockito.anyString())).thenReturn(true);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/authentication/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"ehfg112fs\"}"))
                .andExpect(status().isOk())
                .andReturn();
    }

        @Test
        void testConfirmOtpInvalidToken() throws Exception {

                Mockito.doThrow(new BadRequestException("Token not valid")).when(userService)
                                .userConfirmEmail(Mockito.anyString());

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/confirm")
                                .content("{\"token\":\"ehfg112fs\"}")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Token not valid\"}"))
                                .andReturn();
        }

        @Test
        void testResendToken() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/resend-token")
                                .content("{\"email\":\"test@gmail.com\"}")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testResendTokenWithInvalidEmail() throws Exception {
                String email = "invalidemail@gmail.com";

                Mockito.doThrow(new BadRequestException("Email not valid.")).when(userService)
                                .getUserByEmailIgnorStatus(email);

                mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/authentication/resend-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"invalidemail@gmail.com\"}"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email not valid."));
        }
}
