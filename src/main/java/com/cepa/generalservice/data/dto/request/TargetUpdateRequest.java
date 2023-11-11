package com.cepa.generalservice.data.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TargetUpdateRequest {
    private long targetId;
    private StudentTargetRequest studentTargetRequest;
}
