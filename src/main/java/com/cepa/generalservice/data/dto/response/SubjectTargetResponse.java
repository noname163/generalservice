package com.cepa.generalservice.data.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubjectTargetResponse {
    private long id;
    private long subjectId;
    private String name;
    private double grade;
}
