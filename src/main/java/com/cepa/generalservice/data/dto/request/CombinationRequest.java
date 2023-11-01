package com.cepa.generalservice.data.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CombinationRequest {
    @NotBlank(message = "Subject Name is require")
    @NotNull(message = "Subject Name is require.")
    private String name;
    @NotBlank(message = "Description is require")
    @NotNull(message = "Description is require.")
    private String description;
    @NotNull(message = "Subject is require.")
    private List<Long> subjectIds;
}
