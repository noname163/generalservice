package com.cepa.generalservice.services.userService;

import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;

public interface RegisterService {
    public void teacherRegister(TeacherRegister teacherRegister);

    public void studentRegister(StudentRegister studentRegister);

    public void userConfirmEmail(String token);
}
