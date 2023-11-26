package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyRequest {
    @NotNull(message = "Teacher id is require.")
    private Long teacherId;
    @NotNull(message = "Verify is require.")
    private Boolean verify;
    private String reason;
}
