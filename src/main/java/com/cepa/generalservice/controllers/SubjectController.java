package com.cepa.generalservice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
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
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Get subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get subjects successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
    })
    @GetMapping()
    public ResponseEntity<PaginationResponse<List<SubjectResponse>>> getSubjects(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false, defaultValue = "ASC") SortType sortType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(subjectService.getSubjects(page, size, field, sortType, StateType.TRUE));
    }

    @Operation(summary = "Get subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get subjectId successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "subjectId not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@Valid @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.getSubjectById(id));
    }

}
