package com.cepa.generalservice.services.userService.impl;

import java.util.List;
import java.util.UUID;

import javax.mail.SendFailedException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
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
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInformationMapper userInformationMapper;

    private void studentRegister(UserInformation userInformation, List<Long> subjectIds) {
        studentTargetService.createStudentTarget(userInformation, subjectIds);
    }

    @Transactional
    private void teacherRegister(UserInformation userInformation, Long subjectIds) {
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

        userInformationRepository.findByEmail(userRegister.getEmail()).ifPresent(userInformation -> {
            throw new BadRequestException("Email " + userRegister.getEmail() + " is already exist");
        });

        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            throw new BadRequestException("Password did not match.");
        }

        userRegister.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        UserInformation newUser = userInformationMapper
                .mapDtoToEntity(userRegister);
        newUser.setStatus(UserStatus.WATTING);
        newUser = userInformationRepository.save(newUser);

        if (userRegister.getRole().equals(Role.TEACHER)) {
            teacherRegister(newUser, userRegister.getSubjectId().get(0));
        }
        if (userRegister.getRole().equals(Role.STUDENT)) {
            studentRegister(newUser, userRegister.getSubjectId());
        }
    }

    @Override
    public void userConfirmEmail(String token) {
        UserInformation userInformation = confirmTokenService.getUserByToken(token);
        ConfirmToken userToken = confirmTokenService.getTokenByEmail(userInformation.getEmail());

        if (!userToken.getToken().toString().equals(token)) {
            throw new BadRequestException("Token not valid");
        }

        Boolean confirmStatus = confirmTokenService.verifyToken(token);

        if (Boolean.TRUE.equals(confirmStatus)) {
            userInformation.setStatus(UserStatus.ENABLE);
            userInformationRepository.save(userInformation);
        }

    }

}
