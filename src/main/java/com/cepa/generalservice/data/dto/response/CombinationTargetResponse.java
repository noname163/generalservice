package com.cepa.generalservice.data.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CombinationTargetResponse {
    private long combinationId;
    private double grade;
    private String name;
}
