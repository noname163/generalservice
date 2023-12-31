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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.request.TargetUpdateRequest;
import com.cepa.generalservice.data.dto.request.UpdateSubjectTarget;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.StudentPublicResponse;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.StudentInformationService;
import com.cepa.generalservice.services.studentService.StudentTargetService;
import com.cepa.generalservice.services.subjectTargetService.SubjectTargetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentInformationService studentInformationService;
    @Autowired
    private StudentTargetService studentTargetService;
    @Autowired
    private SubjectTargetService subjectTargetService;

    @Operation(summary = "Get students for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get combinations successfull."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<PaginationResponse<List<StudentResponse>>> getAllStudent(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false, defaultValue = "ASC") SortType sortType,
            @RequestParam(required = false, defaultValue = "ALL") UserStatus userStatus) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentInformationService.getStudents(page, size, field, sortType, userStatus));
    }

    @Operation(summary = "Get student information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get student information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/detail")
    public ResponseEntity<StudentResponse> getStudentInformation() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentInformationService.getStudentInformation());
    }

    @Operation(summary = "Get student targets")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student Target createdsuccessfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })

    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/targets")
    public ResponseEntity<List<StudentTargetResponse>> getStudentTargets() {
        List<StudentTargetResponse> studentTargets = studentTargetService.getStudentTargetsOfCurrentStudent();
        return ResponseEntity.status(HttpStatus.OK).body(studentTargets);
    }

    // @Operation(summary = "Get student target by Id")
    // @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
    // "Student Target createdsuccessfully."),
    // @ApiResponse(responseCode = "400", description = "Bad request.", content = {
    // @Content(mediaType = "application/json", schema = @Schema(implementation =
    // BadRequestException.class)) })

    // })
    // @PreAuthorize("hasAuthority('STUDENT')")
    // @GetMapping("/target/{studentId}/{targetId}")
    // public ResponseEntity<StudentTargetResponse>
    // getStudentTargetById(@PathVariable Long studentId,
    // @PathVariable Long targetId) {
    // StudentTargetResponse studentTarget =
    // studentTargetService.getStudentTargetById(studentId, targetId);
    // return ResponseEntity.status(HttpStatus.OK).body(studentTarget);
    // }

    @Operation(summary = "Create a new Student Target")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/target")
    public ResponseEntity<String> createStudentTarget(@RequestBody StudentTargetRequest studentTargetRequest) {

        studentTargetService.createTarget(studentTargetRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("Student Target created successfully.");
    }

    @Operation(summary = "Edit Student Target")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/edit-target")
    public ResponseEntity<String> editStudentTarget(@RequestBody UpdateSubjectTarget updateSubjectTarget) {

        subjectTargetService.updateSubjectTarget(updateSubjectTarget);

        return ResponseEntity.status(HttpStatus.OK).body("Update Target successfully.");
    }


    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Delete Student Target")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student Target created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @DeleteMapping("/target/{targetId}")
    public ResponseEntity<String> deleteStudentTarget(
            @PathVariable Long targetId) {
        studentTargetService.deleteStudentTarget(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("Student Target deleted successfully.");
    }

    @Operation(summary = "Get Student Subject Target")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Student Subject Target successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectTargetResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/subject-targets/{targetId}")
    public ResponseEntity<List<SubjectTargetResponse>> getStudentTargetSubject(
            @PathVariable Long targetId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectTargetService.getSubjectTargetById(targetId));
    }

    @Operation(summary = "Get Student Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Student Information successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectTargetResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping("/public/detail/{email}")
    public ResponseEntity<StudentPublicResponse> getStudentInformation(
            @PathVariable String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentInformationService.getStudentInformationByEmail(email));
    }
}
