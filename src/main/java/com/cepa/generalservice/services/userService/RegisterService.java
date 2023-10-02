package com.cepa.generalservice.services.userService;

import com.cepa.generalservice.data.dto.request.UserRegister;

public interface RegisterService {
    public void teacherRegister(TeacherRegister teacherRegister);

    public void studentRegister(StudentRegister studentRegister);

    
}
