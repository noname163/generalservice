package com.cepa.generalservice.services.studentService.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.request.StudentTargetRequest;
import com.cepa.generalservice.data.dto.request.TargetUpdateRequest;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.mappers.StudentTargetMapper;
import com.cepa.generalservice.services.studentService.StudentTargetService;

@Service
public class StudentTargetServiceImpl implements StudentTargetService {
    @Autowired
    private StudentTargetRepository studentTargetRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private CombinationRepository combinationRepository;
    @Autowired
    private StudentTargetMapper studentTargetMapper;

    @Override
    @Transactional
    public void createStudentTargets(UserInformation userInformation, List<Long> combinationIds) {
        Set<Long> idSet = combinationIds.stream().collect(Collectors.toSet());

        Optional<List<Combination>> combinations = combinationRepository.findByIdIn(idSet);

        if (!combinations.isPresent() || combinations.get().isEmpty()) {
            throw new BadRequestException("Cannot found combination");
        }

        List<StudentTarget> newStudentTargets = convertCombinationtoStudentTarget(userInformation,
                combinations.get());

        studentTargetRepository.saveAll(newStudentTargets);
    }

    private List<StudentTarget> convertCombinationtoStudentTarget(UserInformation userInformation,
            List<Combination> combinations) {
        List<StudentTarget> existingStudentTargets = studentTargetRepository
                .findByStudentInformation(userInformation);

        List<StudentTarget> studentTargets = new ArrayList<>();
        for (Combination combination : combinations) {
            boolean subjectAlreadyExists = existingStudentTargets.stream()
                    .anyMatch(studentTarget -> studentTarget.getCombination().equals(combination));

            if (subjectAlreadyExists) {
                throw new BadRequestException(
                        "Combination " + combination.getName() + " already exists.");
            }

            studentTargets.add(StudentTarget.builder()
                    .studentInformation(userInformation)
                    .combination(combination)
                    .build());
        }

        return studentTargets;
    }

    @Override
    @Transactional
    public void createTarget(StudentTargetRequest studentTargetRequest) {
        Combination combination = combinationRepository
                .findById(studentTargetRequest.getStudentCombinationTarget().getCombinationId())
                .orElseThrow(() -> new BadRequestException("Combination not found"));
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatus(studentTargetRequest.getStudentCombinationTarget().getStudentId(), UserStatus.ENABLE)
                .orElseThrow(() -> new NotFoundException("User not exist"));
        List<StudentTarget> existingStudentTargets = studentTargetRepository
                .findByStudentInformation(userInformation);

        boolean combinationAlreadyExists = existingStudentTargets.stream()
                .anyMatch(studentTarget -> studentTarget.getCombination().equals(combination));

        if (combinationAlreadyExists) {
            throw new BadRequestException("Combination " + combination.getName() + " already exists.");
        }
        List<SubjectTarget> subjectTargets = new ArrayList<>();
        for (StudentSubjectTargetRequest subjectTargetRequest : studentTargetRequest.getStudentTargetRequest()) {
            SubjectTarget subjectTarget = SubjectTarget.builder()
                    .subjectId(subjectTargetRequest.getSubjectId())
                    .grade(subjectTargetRequest.getGrade())
                    .build();
            subjectTargets.add(subjectTarget);
        }

        StudentTarget studentTarget = StudentTarget
                .builder()
                .combination(combination)
                .studentInformation(userInformation)
                .subjectTargets(subjectTargets)
                .build();

        studentTargetRepository.save(studentTarget);
    }

    @Override
    public List<StudentTargetResponse> getStudentTargetsByStudentId(long studentId) {
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatus(studentId, UserStatus.ENABLE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<StudentTarget> studentTargets = userInformation.getStudentTargets();

        List<StudentTarget> filteredStudentTargets = studentTargets.stream()
                .filter(target -> target.getStateType() == StateType.TRUE)
                .collect(Collectors.toList());

        return studentTargetMapper.mapEntitiesToDtos(filteredStudentTargets);
    }

    @Override
    public void updateTarget(TargetUpdateRequest targetUpdateRequest) {
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatus(
                        targetUpdateRequest.getStudentTargetRequest().getStudentCombinationTarget().getStudentId(),
                        UserStatus.ENABLE)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Combination combination = combinationRepository
                .findByIdAndState(
                        targetUpdateRequest.getStudentTargetRequest().getStudentCombinationTarget().getCombinationId(),
                        true)
                .orElseThrow(() -> new BadRequestException("Combination not found"));
        StudentTarget studentTarget = studentTargetRepository
                .findByIdAndStateType(targetUpdateRequest.getTargetId(), StateType.TRUE)
                .orElseThrow(() -> new NotFoundException("Student Target not found"));

        List<StudentTarget> existingStudentTargets = studentTargetRepository
                .findByStudentInformation(userInformation);
        boolean combinationNameAlreadyExists = existingStudentTargets.stream()
                .anyMatch(target -> target.getCombination().getName().equals(combination.getName()) &&
                        target.getId() != targetUpdateRequest.getTargetId());

        if (combinationNameAlreadyExists) {
            throw new BadRequestException(
                    "Combination with name " + combination.getName()
                            + " already exists for this student.");
        }

        studentTarget.setCombination(combination);
        studentTarget.setGrade(targetUpdateRequest.getStudentTargetRequest().getStudentCombinationTarget().getGrade());

        studentTargetRepository.save(studentTarget);
    }

    @Override
    public void deleteStudentTarget(long studentId, long targetId) {
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatus(studentId, UserStatus.ENABLE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        StudentTarget studentTarget = studentTargetRepository.findByIdAndStateType(targetId, StateType.TRUE)
                .orElseThrow(() -> new NotFoundException("Student Target not found"));

        if (!studentTarget.getStudentInformation().equals(userInformation)) {
            throw new BadRequestException("Student Target does not belong to the specified student.");
        }
        studentTarget.setStateType(StateType.FALSE);
        studentTargetRepository.save(studentTarget);
    }

    @Override
    public StudentTargetResponse getStudentTargetById(long studentId, long targetId) {
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatus(studentId, UserStatus.ENABLE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        StudentTarget studentTarget = studentTargetRepository.findByIdAndStateType(targetId, StateType.TRUE)
                .orElseThrow(() -> new NotFoundException("Student Target not found"));

        if (!studentTarget.getStudentInformation().equals(userInformation)) {
            throw new BadRequestException("Student Target does not belong to the specified student.");
        }

        return studentTargetMapper.mapEntityToDto(studentTarget);
    }
}
