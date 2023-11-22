package com.cepa.generalservice.services.subjectTargetService.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.SubjectTargetResponseInterface;
import com.cepa.generalservice.data.repositories.SubjectTargetRepository;
import com.cepa.generalservice.mappers.SubjectTargetMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.subjectTargetService.SubjectTargetService;

@Service
public class SubjectTargetImpl implements SubjectTargetService {
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    private SubjectTargetRepository subjectTargetRepository;
    @Autowired
    private SubjectTargetMapper subjectTargetMapper;

    @Override
    public List<SubjectTargetResponse> getSubjectTargetById(Long studentTargetId) {
        List<SubjectTargetResponseInterface> subjectTargets = subjectTargetRepository
                .getSubjectTargetsByStudentTargetIdAndUserId(studentTargetId);
        return subjectTargetMapper.mapToStudentTargetResponses(subjectTargets);
    }

    @Override
    public List<SubjectTargetResponse> getListSubjectTargetByTargetId(List<Long> targetIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getListSubjectTargetByTargetId'");
    }

    @Override
    public void createStudentTargetSubject(List<StudentSubjectTargetRequest> studentSubjectTargetRequests,
            StudentTarget studentTarget) {
        List<SubjectTarget> subjectTargets = new ArrayList<>();
        for (StudentSubjectTargetRequest subjectTargetRequest : studentSubjectTargetRequests) {
            SubjectTarget subjectTarget = SubjectTarget.builder()
                    .subjectId(subjectTargetRequest.getSubjectId())
                    .studentTarget(studentTarget)
                    .grade(subjectTargetRequest.getGrade())
                    .build();
            subjectTargets.add(subjectTarget);
        }
        subjectTargetRepository.saveAll(subjectTargets);
    }
}
