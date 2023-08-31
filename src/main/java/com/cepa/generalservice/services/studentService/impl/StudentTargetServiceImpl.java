package com.cepa.generalservice.services.studentService.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.StudentTargetService;

@Service
public class StudentTargetServiceImpl implements StudentTargetService {
    @Autowired
    private StudentTargetRepository studentTargetRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    @Transactional
    public void createStudentTarget(UserInformation userInformation, List<Long> subjectIds) {
        List<Subject> subjects = subjectRepository.findByIdIn(subjectIds);
        List<StudentTarget> existingStudentTargets = studentTargetRepository.findByStudentInformation(userInformation);

        List<StudentTarget> newStudentTargets = new ArrayList<>();
        for (Subject subject : subjects) {
            boolean subjectAlreadyExists = existingStudentTargets.stream()
                    .anyMatch(studentTarget -> studentTarget.getSubject().equals(subject));

            if (subjectAlreadyExists) {
                throw new BadRequestException("Subject " + subject.getName() + " already exists.");
            }

            newStudentTargets.add(StudentTarget.builder()
                    .studentInformation(userInformation)
                    .subject(subject)
                    .build());
        }

        studentTargetRepository.saveAll(newStudentTargets);
    }

}
