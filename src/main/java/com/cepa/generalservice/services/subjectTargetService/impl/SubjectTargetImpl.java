package com.cepa.generalservice.services.subjectTargetService.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.request.UpdateSubjectTarget;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.SubjectTargetResponseInterface;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
import com.cepa.generalservice.data.repositories.SubjectTargetRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
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
    @Lazy
    private StudentTargetService studentTargetService;
    @Autowired
    private StudentTargetRepository studentTargetRepository;
    @Autowired
    private CombinationRepository combinationRepository;
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
        double total = 0;
        Combination combination = studentTarget.getCombination();
        long combinationId = combination.getId();
        List<Long> subjectOfCombinationId = combinationRepository.getSubjectIdsByCombinationId(combinationId);
        boolean isCreate = true;
        for (StudentSubjectTargetRequest subjectTargetRequest : studentSubjectTargetRequests) {
            if (!subjectOfCombinationId.contains(subjectTargetRequest.getSubjectId())) {
                isCreate = false;
                throw new BadRequestException(
                        "Not exist subject with id " + subjectTargetRequest.getSubjectId() + " in combination "
                                + combination.getName());
            }
            SubjectTarget subjectTarget = SubjectTarget.builder()
                    .subjectId(subjectTargetRequest.getSubjectId())
                    .studentTarget(studentTarget)
                    .grade(subjectTargetRequest.getGrade())
                    .build();
            total += subjectTargetRequest.getGrade();
            subjectTargets.add(subjectTarget);
        }
        if (isCreate) {
            studentTarget.setGrade(total);
            studentTargetRepository.save(studentTarget);
            subjectTargetRepository.saveAll(subjectTargets);
        }
    }

    @Override
    public void updateSubjectTarget(UpdateSubjectTarget studentTargetRequest) {
        Long studentId = securityContextService.getCurrentUser().getId();
        if (Boolean.FALSE.equals(studentTargetService.isExistTarget(studentId, studentTargetRequest.getTargetId()))) {
            throw new InValidAuthorizationException("Cannot modify this target");
        }
        List<SubjectTarget> subjectTargets = subjectTargetRepository
                .findByStudentTargetId(studentTargetRequest.getTargetId());
        StudentTarget studentTarget = studentTargetService.getTargetById(studentTargetRequest.getTargetId());
        if (subjectTargets.isEmpty()) {
            createStudentTargetSubject(studentTargetRequest.getStudentSubjectTargetRequests(), studentTarget);
        } else {
            List<SubjectTarget> editSubjectTargets = new ArrayList<>();
            double total = 0;
            for (StudentSubjectTargetRequest subjectTargetRequest : studentTargetRequest
                    .getStudentSubjectTargetRequests()) {
                Long subjectId = subjectTargetRequest.getSubjectId();
                double grade = subjectTargetRequest.getGrade();
                if (grade <= 0 || grade > 10) {
                    throw new BadRequestException("Grade cannot smaller or equal 0 and bigger than 10");
                }
                Optional<SubjectTarget> optionalSubjectTarget = subjectTargets.stream()
                        .filter(target -> target.getSubjectId() == subjectId)
                        .findFirst();
                if (optionalSubjectTarget.isPresent()) {
                    SubjectTarget subjectTarget = optionalSubjectTarget.get();
                    subjectTarget.setGrade(grade);
                    subjectTarget.setUpdateDate(LocalDateTime.now());
                    editSubjectTargets.add(subjectTarget);
                    total += grade;
                } else {
                    throw new NotFoundException("SubjectTarget not found for subjectId: " + subjectId);
                }
            }
            studentTarget.setGrade(total);
            studentTargetRepository.save(studentTarget);
            subjectTargetRepository.saveAll(editSubjectTargets);
        }
    }

}
