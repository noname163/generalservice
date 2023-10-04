package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ForgotPassword {
    @NotBlank(message = "UUID is require")
    public String uuid;
    @NotBlank(message = "Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    public String password;
    @NotBlank(message = "Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    public String confirmPassword;
}
