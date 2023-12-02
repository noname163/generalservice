package com.cepa.generalservice.services.userService.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.Common;
import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.DataConfilictException;
import com.cepa.generalservice.exceptions.InValidInformation;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.studentService.StudentTargetService;
import com.cepa.generalservice.services.userService.RegisterService;

import lombok.Builder;

@Service
@Builder
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentTargetService studentTargetService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInformationMapper userInformationMapper;

    @Override
    @Transactional
    public void teacherRegister(TeacherRegister teacherRegister) {

        UserRegister newUserInformation = teacherRegister.getUserRegister();
        newUserInformation.setRole(Role.TEACHER);
        UserInformation userInformation = userRegister(newUserInformation);

        List<Subject> subjects = subjectRepository.findByIdIn(teacherRegister.getSubjectIds())
                .orElseThrow(() -> new BadRequestException(
                        "Cannot found subject with ID: " + teacherRegister.getSubjectIds().toString()));

        teacherRepository.save(Teacher
                .builder()
                .information(userInformation)
                .subjects(subjects)
                .isValidation(false)
                .build());
    }

    @Override
    @Transactional
    public void studentRegister(StudentRegister studentRegister) {
        UserRegister newUserInformation = studentRegister.getUserRegister();
        newUserInformation.setRole(Role.STUDENT);

        UserInformation userInformation = userRegister(newUserInformation);

        studentTargetService.createStudentTargets(userInformation, studentRegister.getCombinationIds());
    }

    private UserInformation userRegister(UserRegister userRegister) {

        userInformationRepository.findByEmail(userRegister.getEmail()).ifPresent(userInformation -> {
            throw new DataConfilictException("Email already exist.");
        });

        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            throw new InValidInformation("Password did not match.");
        }

        userRegister.setPassword(passwordEncoder.encode(userRegister.getPassword()));

        UserInformation newUser = userInformationMapper.mapDtoToEntity(userRegister);
        newUser.setStatus(UserStatus.WAITTING);
        newUser.setCreateDate(LocalDateTime.now());
        if (userRegister.getRole().equals(Role.STUDENT)) {
            newUser.setImageURL(Common.STUDENT_DEFAULT_AVATAR);
        } else {
            newUser.setImageURL(Common.TEACHER_DEFAULT_AVATAR);
        }
        return userInformationRepository.save(newUser);
    }

}
