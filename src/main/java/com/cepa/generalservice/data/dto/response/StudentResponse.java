package com.cepa.generalservice.data.dto.response;

import java.util.Date;
import java.util.List;

import com.cepa.generalservice.data.constants.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private long id;
    private String email;
    private String fullName;
    private UserStatus userStatus;
    private String description;
    private String url;
    private Date dateOfBirth;
    private List<StudentTargetResponse> targets;
}
