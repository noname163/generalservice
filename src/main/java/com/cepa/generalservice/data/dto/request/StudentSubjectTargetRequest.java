package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentSubjectTargetRequest {
    @NotNull(message = "Target grade is require.")
    private double grade;
    @NotNull(message = "Student id is required")
    private Long subjectId;
}
