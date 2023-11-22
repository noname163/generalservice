package com.cepa.generalservice.data.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponse {
    private long id;
    private String email;
    private String fullName;
    private String url;
    private Date dateOfBirth;
    private LocalDateTime createDate;
}
