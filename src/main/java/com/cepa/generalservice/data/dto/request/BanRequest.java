package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Validation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BanRequest {
    @NotBlank(message = "Reason is require")
    @NotNull(message = "Reason is require.")
    @Size(min = Validation.MIN_LENGTH_REASON, message = "Reason must more than 20 digit")
    private String reason;
    private Long accountId;
}
