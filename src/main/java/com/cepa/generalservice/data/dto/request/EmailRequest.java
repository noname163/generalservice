package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cepa.generalservice.data.constants.Validation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailRequest {
    @NotBlank(message = "Email is require")
    @NotNull(message = "Email is require.")
    @Pattern(regexp = Validation.EMAIL_REGEX, message = "Email must in format abc@domain.com")
    private String email;
    @JsonIgnore
    private String test;
}
