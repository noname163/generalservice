package com.cepa.generalservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.StudentInformationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@PreAuthorize("hasAuthority('STUDENT')")
@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentInformationService studentInformationService;

    @Operation(summary = "Get student information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get student information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping("/{email}")
    public ResponseEntity<StudentResponse> getStudentInformation(@PathVariable String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentInformationService.getStudentByEmail(email));
    }
}
