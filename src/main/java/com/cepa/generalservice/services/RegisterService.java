package com.cepa.generalservice.services;

import java.util.List;

import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.UserInformation;

public interface RegisterService {
    public void studentRegister(UserInformation userInformation, List<Long> subjectIds);
    public void teacherRegister(UserInformation userInformation, Long subjectIds);
    public void userRegister(UserRegister userRegister);
}
