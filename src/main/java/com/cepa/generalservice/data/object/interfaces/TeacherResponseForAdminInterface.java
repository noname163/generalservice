package com.cepa.generalservice.data.object.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.cepa.generalservice.data.constants.UserStatus;

public interface TeacherResponseForAdminInterface {
    long getId();

    String getEmail();

    String getFullName();

    String getUrl();

    LocalDateTime getCreateDate();

    UserStatus getStatus();

    List<String> getSubject();

    Date getDateOfBirth();

    String getIdentify();
}
