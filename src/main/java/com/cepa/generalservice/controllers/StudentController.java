package com.cepa.generalservice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.CombinationRequest;
import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.StudentInformationService;
import com.cepa.generalservice.services.studentService.StudentTargetService;

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
        @Autowired
        private StudentTargetService studentTargetService;

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

        @Operation(summary = "Get student targets")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student Target createdsuccessfully."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })

        })
        @GetMapping("/{studentId}/targets")
        public ResponseEntity<List<StudentTargetResponse>> getStudentTargets(@PathVariable Long studentId) {
                List<StudentTargetResponse> studentTargets = studentTargetService
                                .getStudentTargetsByStudentId(studentId);
                return ResponseEntity.status(HttpStatus.OK).body(studentTargets);
        }

        @Operation(summary = "Get student target by Id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student Target createdsuccessfully."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })

        })
        @GetMapping("/{studentId}/target/{targetId}")
        public ResponseEntity<StudentTargetResponse> getStudentTargetById(@PathVariable Long studentId,
                        @PathVariable Long targetId) {
                StudentTargetResponse studentTarget = studentTargetService.getStudentTargetById(studentId, targetId);
                return ResponseEntity.status(HttpStatus.OK).body(studentTarget);
        }

        @Operation(summary = "Create a new Student Target")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PostMapping("/{studentId}/target")
        public ResponseEntity<String> createStudentTarget(@Valid @PathVariable Long studentId,
                        @RequestBody StudentTargetRequest studentTargetRequest) {

                studentTargetService.createTarget(studentId, studentTargetRequest);

                return ResponseEntity.status(HttpStatus.OK).body("Student Target created successfully.");
        }

        @Operation(summary = "Update Student Target")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PutMapping("/{studentId}/target/{targetId}")
        public ResponseEntity<String> editStudentTarget(
                        @PathVariable Long studentId,
                        @PathVariable Long targetId,
                        @Valid @RequestBody StudentTargetRequest studentTargetRequest) {
                studentTargetService.updateTarget(studentId, targetId, studentTargetRequest);
                return ResponseEntity.status(HttpStatus.OK).body("Student Target edited successfully.");
        }

        @Operation(summary = "Delete Student Target")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @DeleteMapping("/{studentId}/target/{targetId}")
        public ResponseEntity<String> deleteStudentTarget(
                        @PathVariable Long studentId,
                        @PathVariable Long targetId) {
                studentTargetService.deleteStudentTarget(studentId, targetId);
                return ResponseEntity.status(HttpStatus.OK).body("Student Target deleted successfully.");
        }
}
