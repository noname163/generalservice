package com.cepa.generalservice.data.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CombinationResponse {
    private long id;
    private String name;
    private String description;
    private List<SubjectResponse> subjects;
}
