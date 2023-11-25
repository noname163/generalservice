package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.Validation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRegister {
    @NotBlank(message = "Email is require")
    @NotNull(message = "Email is require.")
    @Pattern(regexp = Validation.EMAIL_REGEX, message = "Email must in format abc@domain.com")
    private String email;

    @NotBlank(message = "Full name is require.")
    @NotNull(message = "Full name is require.")
    @Pattern(regexp = Validation.ONLY_ALPHABET_REGEX, message = "Full name containt alphabet only.")
    @Size(min = Validation.MIN_LENGTH_FULLNAME, message = "Full name must more than 6 digit")
    private String fullName;

    @NotBlank(message = "Password is require.")
    @NotNull(message = "Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    private String password;

    @NotBlank(message = "Password is require.")
    @NotNull(message = "Password is require.")
    @Size(min = Validation.MIN_LENGTH_PASSWORD, message = "Password must more than 6 digit")
    private String confirmPassword;

    private String desciption;

    @JsonIgnore
    private Role role;
}
