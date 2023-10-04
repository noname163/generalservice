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
public class TeacherRegister {
    @Valid
    private UserRegister userRegister;

    @NotNull(message = "Subject is require.")
    private List<Long> subjectIds;
}
