package com.cepa.generalservice.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.ChangePasswordRequest;
import com.cepa.generalservice.data.dto.request.CombinationRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.UserRequest;
import com.cepa.generalservice.data.dto.response.AdminEditUserStatus;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.mappers.TeacherMapper;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.userService.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserController {
        @Autowired
        private UserService userService;
        @Autowired
        private UserInformationMapper userInformationMapper;

        @Operation(summary = "Get user information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Get user information successfull.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformation.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @GetMapping("/{email}")
        public ResponseEntity<UserResponse> getUserInformation(@PathVariable String email) {
                UserInformation user = userService.getUserByEmail(email);
                UserResponse userResponse = userInformationMapper.mapEntityToDto(user);
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(userResponse);
        }

        @Operation(summary = "Update user information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Update user information successfull.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformation.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PutMapping("update/{email}")
        public ResponseEntity<UserResponse> UpdateUserInformation(@Valid @PathVariable String email,
                        @RequestBody UserRequest userRequest) {
                userService.updateUserByEmail(email, userRequest);

                return ResponseEntity.status(HttpStatus.OK).build();
        }

        @Operation(summary = "Change password")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Change password successfull."),
                        @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PatchMapping("/change-password")
        public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
                userService.changePassword(changePasswordRequest);
                return ResponseEntity.status(HttpStatus.OK).body("Change password successfully");
        }

        @Operation(summary = "Change password")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Change status successfull."),
                        @ApiResponse(responseCode = "400", description = "User not valid.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PreAuthorize("hasAuthority('ADMIN')")
        @PatchMapping("/change-user-status")
        public ResponseEntity<String> editUserStatus(@Valid @RequestBody AdminEditUserStatus adminEditUserStatus) {
                userService.editUserStatus(adminEditUserStatus);
                return ResponseEntity.status(HttpStatus.OK).body("Change status successfully");
        }
}
