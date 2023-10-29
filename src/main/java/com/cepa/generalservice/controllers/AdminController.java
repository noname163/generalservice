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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.request.SubjectRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.services.subjectService.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Create a new subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject created successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) }) })
    @PostMapping("create/subject")
    public ResponseEntity<Void> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        subjectService.createSubject(subjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update Subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject .", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) }) })
    @PutMapping("update/{id}")
    public ResponseEntity<Void> updateSubject(@Valid @PathVariable Long id,
            @RequestBody SubjectRequest subjectRequest) {
        subjectService.updateSubject(id, subjectRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Delete Subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get subjects successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @DeleteMapping("subject/{id}")
    public ResponseEntity<Void> deleteSubject(@Valid @PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok().build();
    }
}
