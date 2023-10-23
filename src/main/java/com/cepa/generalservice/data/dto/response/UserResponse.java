package com.cepa.generalservice.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
}
