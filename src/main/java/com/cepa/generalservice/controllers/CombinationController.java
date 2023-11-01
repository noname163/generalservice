package com.cepa.generalservice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.services.combinationService.CombinationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/combinations")
public class CombinationController {
        @Autowired
        private CombinationService combinationService;

        @Operation(summary = "Get combinations")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Get combinations successfull."),
                        @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)) })
        })
        @GetMapping()
        public ResponseEntity<PaginationResponse<List<CombinationResponse>>> getCombinations(
                        @RequestParam(required = false, defaultValue = "0") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer size,
                        @RequestParam(required = false) String field,
                        @RequestParam(required = false, defaultValue = "ASC") SortType sortType) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(combinationService.getCombination(page, size, field, sortType, StateType.TRUE));
        }

        @Operation(summary = "Get combination by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Get combinationId successfully.", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "combinationId not found", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)) })
        })
        @GetMapping("/{id}")
        public ResponseEntity<CombinationResponse> getSubjectById(@Valid @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(combinationService.getCombinationById(id));
        }

}