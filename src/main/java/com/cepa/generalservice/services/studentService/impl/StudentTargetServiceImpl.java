package com.cepa.generalservice.services.studentService.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.StudentTargetService;

@Service
public class StudentTargetServiceImpl implements StudentTargetService {
    @Autowired
    private StudentTargetRepository studentTargetRepository;
    @Autowired
    private CombinationRepository combinationRepository;

    @Override
    @Transactional
    public void createStudentTarget(UserInformation userInformation, List<Long> combinationIds) {
        List<Combination> combinations = combinationRepository
        .findByIdIn(combinationIds)
        .orElseThrow(() -> new BadRequestException("Cannot found combination"));
        List<StudentTarget> existingStudentTargets = studentTargetRepository.findByStudentInformation(userInformation);

        List<StudentTarget> newStudentTargets = new ArrayList<>();
        for (Combination combination : combinations) {
            boolean subjectAlreadyExists = existingStudentTargets.stream()
                    .anyMatch(studentTarget -> studentTarget.getCombination().equals(combination));

            if (subjectAlreadyExists) {
                throw new BadRequestException("Subject " + combination.getName() + " already exists.");
            }

            newStudentTargets.add(StudentTarget.builder()
                    .studentInformation(userInformation)
                    .combination(combination)
                    .build());
        }

        studentTargetRepository.saveAll(newStudentTargets);
    }

}
