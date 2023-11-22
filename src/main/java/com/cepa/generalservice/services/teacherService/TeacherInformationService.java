package com.cepa.generalservice.services.teacherService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;

public interface TeacherInformationService {
    public TeacherResponse getTeacherInformation();
    public TeacherResponse getTeacherInformationByEmail(String email);

    public PaginationResponse<List<TeacherResponse>> getTeachers(Integer page, Integer size, String field,
            SortType sortType, UserStatus userStatus);
}
