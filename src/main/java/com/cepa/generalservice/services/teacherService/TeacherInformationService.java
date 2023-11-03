package com.cepa.generalservice.services.teacherService;

import com.cepa.generalservice.data.dto.response.TeacherResponse;

public interface TeacherInformationService {
    public TeacherResponse getTeacherByEmail(String email);
}
