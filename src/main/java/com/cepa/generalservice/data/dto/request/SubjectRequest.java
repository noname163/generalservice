package com.cepa.generalservice.data.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SubjectRequest {
    private String name;
    private String url;
    private String description;
}
