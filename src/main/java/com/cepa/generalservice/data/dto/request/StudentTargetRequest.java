package com.cepa.generalservice.data.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentTargetRequest {
    StudentCombinationTarget studentCombinationTarget;
    List<StudentSubjectTargetRequest> studentTargetRequest;
}
