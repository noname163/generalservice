package com.cepa.generalservice.services.studentService;

import java.util.List;

import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.request.TargetUpdateRequest;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.UserInformation;

public interface StudentTargetService {
    public void createStudentTargets(UserInformation userInformation, List<Long> combinationIds);

    public List<StudentTargetResponse> getStudentTargetsOfCurrentStudent();

    public void createTarget(StudentTargetRequest studentTargetRequest);

    public void updateTarget(TargetUpdateRequest targetUpdateRequest);

    public void deleteStudentTarget(long studentId, long targetId);

    public StudentTargetResponse getStudentTargetById(long studentId, long targetId);

    
}
