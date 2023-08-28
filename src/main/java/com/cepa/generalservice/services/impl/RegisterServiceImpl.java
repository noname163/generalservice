package com.cepa.generalservice.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.RegisterService;

public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInformationMapper userInformationMapper;

    @Override
    public void studentRegister(UserInformation userInformation, List<Long> subjectIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'studentRegister'");
    }

    @Override
    @Transactional
    public void teacherRegister(UserInformation userInformation, Long subjectIds) {
        teacherRepository.findByInformationId(userInformation.getId())
                .ifPresent(teacher -> {
                    throw new BadRequestException("Teacher information exists");
                });
        Subject subject = subjectRepository.findById(subjectIds)
                .orElseThrow(() -> new BadRequestException("Cannot found subject with ID: " + subjectIds));

        teacherRepository.save(Teacher
                .builder()
                .information(userInformation)
                .subject(subject)
                .build());
    }

    @Override
    @Transactional
    public void userRegister(UserRegister userRegister) {
        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            throw new BadRequestException("Password did not match.");
        }
        userRegister.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        UserInformation newUser = userInformationRepository
                .save(userInformationMapper
                        .mapDtoToEntity(userRegister));
        if (userRegister.getRole().equals(Role.TEACHER)) {
            teacherRegister(newUser, userRegister.getSubjectId().get(0));
        }
        if (userRegister.getRole().equals(Role.STUDENT)) {
            studentRegister(newUser, userRegister.getSubjectId());
        }
    }

}
