package com.cepa.generalservice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.constants.Validation;
import com.cepa.generalservice.data.dto.request.EmailRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.request.TokenRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.event.EventPublisher;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.userService.RegisterService;
import com.cepa.generalservice.services.userService.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventPublisher eventPublisher;

    @Operation(summary = "Create new teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/register/teacher")
    public ResponseEntity<Void> createTeacherAccount(@Valid @RequestBody TeacherRegister teacherRegister) {
        registerService.teacherRegister(teacherRegister);
        eventPublisher.publishEvent(teacherRegister.getUserRegister().getEmail(),
                teacherRegister.getUserRegister().getFullName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Create new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/register/student")
    public ResponseEntity<Void> createStudentAccount(@Valid @RequestBody StudentRegister studentRegister) {
        registerService.studentRegister(studentRegister);
        eventPublisher.publishEvent(studentRegister.getUserRegister().getEmail(),
                studentRegister.getUserRegister().getFullName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.login(loginRequest));
    }

    @Operation(summary = "Forgot password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PatchMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody EmailRequest email) {
        UserInformation userInformation = userService.getUserByEmail(email.getEmail());
        eventPublisher.publishEvent(userInformation.getEmail(), userInformation.getFullName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reset password successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ForgotPassword forgotPassword) {
        userService.forgotPassword(forgotPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Verify token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verify successfull."),
            @ApiResponse(responseCode = "400", description = "Token not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmOtp(@Valid  @RequestBody TokenRequest token) {
        userService.userConfirmEmail(token.getToken());
        eventPublisher.publishEvent(token.getToken());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Resend token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resend successfull."),
            @ApiResponse(responseCode = "400", description = "Email not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PatchMapping("/resend-token")
    public ResponseEntity<Void> resendToken(@Valid @RequestBody EmailRequest email) {
        UserInformation userInformation = userService.getUserByEmailIgnorStatus(email.getEmail());
        eventPublisher.publishEvent(userInformation);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verify successfull."),
            @ApiResponse(responseCode = "400", description = "Token not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> reFreshToken(HttpServletRequest request) {
        return ResponseEntity.ok().body(authenticationService.reFreshToken(request));
    }

    @Operation(summary = "Logout ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody EmailRequest email) {
        authenticationService.logout(email.getEmail());
        return ResponseEntity.ok().build();
    }

}
