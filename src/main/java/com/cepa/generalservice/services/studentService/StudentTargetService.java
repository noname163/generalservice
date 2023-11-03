package com.cepa.generalservice.services.studentService;

import java.util.List;

import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;

public interface StudentTargetService {
    public void createStudentTarget(UserInformation userInformation, List<Long> combinationIds);

    public List<StudentTargetResponse> getStudentTargetsByStudentId(long studentId);

    public void createTarget(long id, StudentTargetRequest studentTargetRequest);

    public void updateTarget(long studentId, long targetId, StudentTargetRequest studentTargetRequest);

    public void deleteStudentTarget(long studentId, long targetId);

    public StudentTargetResponse getStudentTargetById(long studentId, long targetId);
}
