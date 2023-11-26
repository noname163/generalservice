package com.cepa.generalservice.services.teacherService;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.EditTeacherRequest;
import com.cepa.generalservice.data.dto.request.VerifyRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponseForAdmin;

public interface TeacherInformationService {
    public TeacherResponse getTeacherInformation();

    public TeacherResponse getTeacherInformationByEmail(String email);

    public void editTeacherInformation(EditTeacherRequest editTeacherRequest, MultipartFile indentify);

    public PaginationResponse<List<TeacherResponse>> getTeachers(Integer page, Integer size, String field,
            SortType sortType, UserStatus userStatus);

    public PaginationResponse<List<TeacherResponseForAdmin>> getListVerifyTeacher(Integer page, Integer size,
            String field,
            SortType sortType);

    public TeacherResponseForAdmin getTeacherVerifyById(Long id);

    public void verifyTeacher(VerifyRequest verifyRequest);
}
