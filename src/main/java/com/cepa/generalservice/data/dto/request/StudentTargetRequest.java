package com.cepa.generalservice.data.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentTargetRequest {
    @NotNull(message = "Combination is required")
    private Long combinationId;
    List<StudentSubjectTargetRequest> studentTargetRequest;
}
