package com.cepa.generalservice.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.exceptions.BadRequestException;
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

    @Operation(summary = "Create new basic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfull."),
            @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))})
    })
    @PostMapping("/register")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody UserRegister userRegister){
        registerService.userRegister(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
