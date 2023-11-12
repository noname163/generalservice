package com.cepa.generalservice.data.dto.response;

import java.time.LocalDateTime;
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
public class TeacherResponse {
    private long id;
    private String email;
    private String fullName;
    private String url;
    private LocalDateTime createDate;
    private UserStatus status;
    private String subject;
    private Date dateOfBirth;
}
