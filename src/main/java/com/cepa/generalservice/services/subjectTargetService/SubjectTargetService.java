package com.cepa.generalservice.services.subjectTargetService;

import java.util.List;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.request.UpdateSubjectTarget;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;

public interface SubjectTargetService {
    public List<SubjectTargetResponse> getListSubjectTargetByTargetId(List<Long> targetIds);
    public List<SubjectTargetResponse> getSubjectTargetById(Long studentTargetId);
    public void createStudentTargetSubject(List<StudentSubjectTargetRequest> studentSubjectTargetRequests,  StudentTarget studentTarget);
    public void updateSubjectTarget(UpdateSubjectTarget studentTargetRequest);
}
