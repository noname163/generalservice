package com.cepa.generalservice.data.object.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.cepa.generalservice.data.constants.UserStatus;

public interface TeacherResponseInterface {
    long getId();
    String getEmail();
    String getFullName();
    String getUrl();
    LocalDateTime getCreateDate();
    String getDescription();
    UserStatus getStatus();
    List<String> getSubject();
    Date getDateOfBirth();
    Boolean getIsVerify();
}
