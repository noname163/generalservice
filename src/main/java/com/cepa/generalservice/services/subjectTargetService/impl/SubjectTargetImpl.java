package com.cepa.generalservice.services.subjectTargetService.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.request.UpdateSubjectTarget;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.SubjectTargetResponseInterface;
import com.cepa.generalservice.data.repositories.SubjectTargetRepository;
import com.cepa.generalservice.exceptions.InValidAuthorizationException;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.mappers.SubjectTargetMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.studentService.StudentTargetService;
import com.cepa.generalservice.services.subjectTargetService.SubjectTargetService;

@Service
public class SubjectTargetImpl implements SubjectTargetService {
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    private SubjectTargetRepository subjectTargetRepository;
    @Autowired
    private StudentTargetService studentTargetService;
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

    @Override
    public void updateSubjectTarget(UpdateSubjectTarget studentTargetRequest) {
        Long studentId = securityContextService.getCurrentUser().getId();
        if (studentTargetService.isExistTarget(studentId, studentTargetRequest.getTargetId())) {
            throw new InValidAuthorizationException("Cannot modify this target");
        }
        List<SubjectTarget> subjectTargets = subjectTargetRepository
                .findByStudentTargetId(studentTargetRequest.getTargetId());
        List<SubjectTarget> editSubjectTargets = new ArrayList<>();
        for (StudentSubjectTargetRequest subjectTargetRequest : studentTargetRequest
                .getStudentSubjectTargetRequests()) {
            Long subjectId = subjectTargetRequest.getSubjectId();
            double grade = subjectTargetRequest.getGrade();
            Optional<SubjectTarget> optionalSubjectTarget = subjectTargets.stream()
                    .filter(target -> target.getSubjectId() == subjectId)
                    .findFirst();
            if (optionalSubjectTarget.isPresent()) {
                SubjectTarget subjectTarget = optionalSubjectTarget.get();
                subjectTarget.setGrade(grade);
                subjectTarget.setUpdateDate(LocalDateTime.now());
                editSubjectTargets.add(subjectTarget);
            } else {
                throw new NotFoundException("SubjectTarget not found for subjectId: " + subjectId);
            }
        }
        subjectTargetRepository.saveAll(editSubjectTargets);
    }

}
