package com.cepa.generalservice.services.studentService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.StudentPublicResponse;
import com.cepa.generalservice.data.dto.response.StudentResponse;

public interface StudentInformationService {

    public StudentResponse getStudentInformation();

    public StudentPublicResponse getStudentInformationByEmail(String email);

    public PaginationResponse<List<StudentResponse>> getStudents(Integer page, Integer size, String field,
            SortType sortType, UserStatus userStatus);
}
