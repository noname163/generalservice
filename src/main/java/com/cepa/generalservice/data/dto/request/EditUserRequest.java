package com.cepa.generalservice.data.dto.request;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Validation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditUserRequest {

    @Pattern(regexp = Validation.ONLY_ALPHABET_REGEX, message = "Full name containt alphabet only.")
    @Size(min = Validation.MIN_LENGTH_FULLNAME, message = "Full name must more than 6 digit")
    private String fullName;

    private Date dateOfBirth;

    private String desciption;
}
