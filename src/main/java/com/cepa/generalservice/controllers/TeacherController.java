package com.cepa.generalservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.EditTeacherRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponseForAdmin;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    private TeacherInformationService teacherInformationService;

    @Operation(summary = "Get teachers for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get combinations successfull."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<PaginationResponse<List<TeacherResponse>>> getCombinations(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false, defaultValue = "ASC") SortType sortType,
            @RequestParam(required = false, defaultValue = "ALL") UserStatus userStatus) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherInformationService.getTeachers(page, size, field, sortType, userStatus));
    }

    @Operation(summary = "Get teacher information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get teacher information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/detail")
    public ResponseEntity<TeacherResponse> getTeacherInformation() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherInformationService.getTeacherInformation());
    }

    @Operation(summary = "Get teacher information for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get teacher information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping("/detail/user")
    public ResponseEntity<TeacherResponse> getTeacherInformationForUser(
            @RequestParam(required = true) String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherInformationService.getTeacherInformationByEmail(email));
    }
    @Operation(summary = "Get teacher information for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get teacher information successfull.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/verification-list")
    public ResponseEntity<PaginationResponse<List<TeacherResponseForAdmin>>> getTeacherVerificationInformation(@RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false, defaultValue = "ASC") SortType sortType) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherInformationService.getListVerifyTeacher(page, size, field, sortType));
    }

    @Operation(summary = "Edit teacher information ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit teacher information successfull."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/edit-information")
    public ResponseEntity<Void> editCurrentTeacherInformation(@RequestPart EditTeacherRequest editTeacherRequest,
            @RequestPart(required = false) MultipartFile indentify) {
        teacherInformationService.editTeacherInformation(editTeacherRequest, indentify);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
