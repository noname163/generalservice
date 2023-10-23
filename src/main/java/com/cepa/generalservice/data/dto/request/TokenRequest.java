package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenRequest {
    @NotBlank(message = "Token is require")
    @NotNull(message = "Token is require.")
    private String token;
    @JsonIgnore
    private String test;
}
