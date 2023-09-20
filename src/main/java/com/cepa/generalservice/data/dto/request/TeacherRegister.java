package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TeacherRegister {
    private UserRegister userRegister;

    @NotNull(message = "Subject is require.")
    private Long subjectId;
}
