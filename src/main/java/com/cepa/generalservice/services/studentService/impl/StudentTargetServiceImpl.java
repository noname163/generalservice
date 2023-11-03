package com.cepa.generalservice.services.studentService.impl;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Collector;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
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
    public void createStudentTargets(UserInformation userInformation, List<Long> combinationIds) {
        Set<Long> idSet = combinationIds.stream().collect(Collectors.toSet());

        Optional<List<Combination>> combinations = combinationRepository.findByIdIn(idSet);

        if (!combinations.isPresent() || combinations.get().isEmpty()) {
            throw new BadRequestException("Cannot found combination");
        }
        
        List<StudentTarget> newStudentTargets = convertCombinationtoStudentTarget(userInformation, combinations.get());

        studentTargetRepository.saveAll(newStudentTargets);
    }


    private List<StudentTarget> convertCombinationtoStudentTarget(UserInformation userInformation, List<Combination> combinations) {
        List<StudentTarget> existingStudentTargets = studentTargetRepository
                .findByStudentInformation(userInformation);

        List<StudentTarget> studentTargets = new ArrayList<>();
        for (Combination combination : combinations) {
            boolean subjectAlreadyExists = existingStudentTargets.stream()
                    .anyMatch(studentTarget -> studentTarget.getCombination().equals(combination));

            if (subjectAlreadyExists) {
                throw new BadRequestException("Combination " + combination.getName() + " already exists.");
            }

            studentTargets.add(StudentTarget.builder()
                    .studentInformation(userInformation)
                    .combination(combination)
                    .build());
        }
        return studentTargets;
    }

}
