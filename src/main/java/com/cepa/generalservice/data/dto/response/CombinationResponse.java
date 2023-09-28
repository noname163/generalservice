package com.cepa.generalservice.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CombinationResponse {
    private long id;
    private String name;
    private String url;
    private String description;
}
