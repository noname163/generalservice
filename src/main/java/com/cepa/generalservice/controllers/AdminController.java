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
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.request.CombinationRequest;
import com.cepa.generalservice.data.dto.request.SubjectRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.combinationService.CombinationService;
import com.cepa.generalservice.services.subjectService.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin")
public class AdminController {

        @Autowired
        private SubjectService subjectService;
        @Autowired
        private CombinationService combinationService;

        @Operation(summary = "Get subjects")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Get subjects successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @GetMapping("subjects")
        public ResponseEntity<PaginationResponse<List<SubjectResponse>>> getSubjects(
                        @RequestParam(required = false, defaultValue = "0") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer size,
                        @RequestParam(required = false) String field,
                        @RequestParam(required = false, defaultValue = "ASC") SortType sortType,
                        @RequestParam(required = false, defaultValue = "ALL") StateType stateType) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(subjectService.getSubjects(page, size, field, sortType, stateType));
        }

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
        @PutMapping("subject/update/{id}")
        public ResponseEntity<Void> updateSubject(@Valid @PathVariable Long id,
                        @RequestBody SubjectRequest subjectRequest) {
                subjectService.updateSubject(id, subjectRequest);
                return ResponseEntity.status(HttpStatus.OK).build();
        }

        @Operation(summary = "Delete Subject by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Delete subjectId successfully.", content = {
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

        @Operation(summary = "Active State Subject by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Active subjectId successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PutMapping("subject/activeState/{id}")
        public ResponseEntity<Void> activeStateSubject(@Valid @PathVariable Long id) {
                subjectService.activeStateSubject(id);
                return ResponseEntity.ok().build();
        }

        // ================================================ Combination ===============
        @Operation(summary = "Get combinations")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Get combinations successfull."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @GetMapping("combinations")
        public ResponseEntity<PaginationResponse<List<CombinationResponse>>> getCombinations(
                        @RequestParam(required = false, defaultValue = "0") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer size,
                        @RequestParam(required = false) String field,
                        @RequestParam(required = false, defaultValue = "ASC") SortType sortType,
                        @RequestParam(required = false, defaultValue = "ALL") StateType stateType) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(combinationService.getCombination(page, size, field, sortType, stateType));
        }

        @Operation(summary = "Create a new combination")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Combination created successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectResponse.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) }) })
        @PostMapping("create/combination")
        public ResponseEntity<Void> createSubject(@Valid @RequestBody CombinationRequest combinationRequest) {
                combinationService.createCombination(combinationRequest);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @Operation(summary = "Update Combination by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Combination .", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectResponse.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) }) })
        @PutMapping("combination/update/{id}")
        public ResponseEntity<Void> updateSubject(@Valid @PathVariable Long id,
                        @RequestBody CombinationRequest combinationRequest) {
                combinationService.updateCombination(id, combinationRequest);
                return ResponseEntity.status(HttpStatus.OK).build();
        }

        @Operation(summary = "Delete Combination by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Delete combinationId successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @DeleteMapping("combination/{id}")
        public ResponseEntity<Void> deleteCombination(@Valid @PathVariable Long id) {
                combinationService.deleteCombination(id);
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Active State Combination by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Active combinationId successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @PutMapping("/activeState/{id}")
        public ResponseEntity<Void> activeStateCombination(@Valid @PathVariable Long id) {
                combinationService.activeStateCombination(id);
                return ResponseEntity.ok().build();
        }
}
