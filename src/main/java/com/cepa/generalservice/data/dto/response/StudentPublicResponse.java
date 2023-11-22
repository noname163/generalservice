package com.cepa.generalservice.data.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentPublicResponse {
    private UserResponse userResponse;
    private List<StudentTargetResponse> targets;
}
