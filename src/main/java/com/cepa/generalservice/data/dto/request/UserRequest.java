package com.cepa.generalservice.data.dto.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "User Name is require")
    @NotNull(message = "User Name is require.")
    private String fullName;
    private String url;
    private Date dateOfBirth;
}