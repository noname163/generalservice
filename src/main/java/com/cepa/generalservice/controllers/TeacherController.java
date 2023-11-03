package com.cepa.generalservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@PreAuthorize("hasAuthority('TEACHER')")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    private TeacherInformationService teacherInformationService;

    @Operation(summary = "Get teacher information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get teacher information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping("/{email}")
    public ResponseEntity<TeacherResponse> getTeacherInformation(@PathVariable String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherInformationService.getTeacherByEmail(email));
    }
}
