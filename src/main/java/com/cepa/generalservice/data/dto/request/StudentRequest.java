package com.cepa.generalservice.data.dto.request;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentRequest {
    private String email;
    private String fullName;
    private String url;
    private Date dateOfBirth;
    private List<StudentTargetRequest> targets;
}
