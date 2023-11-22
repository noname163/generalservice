package com.cepa.generalservice.data.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentTargetResponse {
    private long id;
    private String name;
    private double grade;
    private List<SubjectTargetResponse> subjectTargetResponses;
}
