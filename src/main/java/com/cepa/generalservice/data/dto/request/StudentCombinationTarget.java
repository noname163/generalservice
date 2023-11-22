package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentCombinationTarget {
    @NotNull(message = "Combination is required")
    private Long combinationId;
    private Double grade;
}
