package com.cepa.generalservice.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SubjectRequest {
    @NotBlank(message = "Subject Name is require")
    @NotNull(message = "Subject Name is require.")
    private String name;
    @NotBlank(message = "Url is require")
    @NotNull(message = "Url is require.")
    private String url;
    private String description;
}
