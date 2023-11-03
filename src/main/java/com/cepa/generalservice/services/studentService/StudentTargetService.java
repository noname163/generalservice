package com.cepa.generalservice.services.studentService;

import java.util.List;

import com.cepa.generalservice.data.entities.UserInformation;

public interface StudentTargetService {
    public void createStudentTargets(UserInformation userInformation, List<Long> subjectIds);
}
