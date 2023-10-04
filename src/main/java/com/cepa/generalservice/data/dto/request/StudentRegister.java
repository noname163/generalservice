package com.cepa.generalservice.data.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class StudentRegister {
    @Valid
    private UserRegister userRegister;

    @NotNull(message = "Combination is require.")
    private List<Long> combinationIds;

}
