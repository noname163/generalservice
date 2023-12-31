package com.cepa.generalservice.data.dto.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.cepa.generalservice.data.constants.UserStatus;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
@Data
@Setter
public class TeacherResponseForAdmin {
    private long id;
    private String email;
    private String fullName;
    private String description;
    private String url;
    private LocalDateTime createDate;
    private UserStatus status;
    private List<String> subject;
    private Date dateOfBirth;
    private String indentify;
}
