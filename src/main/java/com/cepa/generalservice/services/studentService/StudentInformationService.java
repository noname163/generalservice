package com.cepa.generalservice.services.studentService;

import com.cepa.generalservice.data.dto.response.StudentResponse;

public interface StudentInformationService {
    public StudentResponse getStudentByEmail(String email);
}
