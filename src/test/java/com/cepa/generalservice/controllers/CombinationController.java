package com.cepa.generalservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.exceptions.BadRequestException;
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
    public ResponseEntity<PaginationResponse<List<CombinationResponse>>> getSubjects(@RequestBody PaginationRequest paginationRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(combinationService.getCombination(paginationRequest));
    }
}