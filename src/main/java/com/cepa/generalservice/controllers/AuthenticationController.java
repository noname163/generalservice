package com.cepa.generalservice.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.event.EventPublisher;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.userService.RegisterService;

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
    private EventPublisher eventPublisher;

    @Operation(summary = "Create new basic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PostMapping("/register")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody UserRegister userRegister) {
        registerService.userRegister(userRegister);
        eventPublisher.publishEvent(userRegister.getEmail(), userRegister.getFullName());
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

    @Operation(summary = "Verify token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verify successfull."),
            @ApiResponse(responseCode = "400", description = "Token not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmOtp(@RequestParam(name="token") String token){
        registerService.userConfirmEmail(token);
        return ResponseEntity.ok().build();
    }

}
