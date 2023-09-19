package com.cepa.generalservice.data.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.Validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class StudentRegister {
    
    private UserRegister userRegister;

    @NotNull(message = "Combination is require.")
    private List<Long> combinationIds;

}
