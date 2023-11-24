package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Password is require")
    @NotNull(message = "Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    private String oldPassword;
    @NotBlank(message = "New Password is require")
    @NotNull(message = "New Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    private String newPassword;
    @NotBlank(message = "Confirm Password is require")
    @NotNull(message = "Confirm Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    private String confirmPassword;
}
