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
import com.cepa.generalservice.data.dto.request.StudentRegister;
import com.cepa.generalservice.data.dto.request.TeacherRegister;
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
    private SendEmailService sendEmailService;
    @Autowired
    private SecurityContextService securityContextService;
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

        Subject subject = subjectRepository.findById(teacherRegister.getSubjectId())
                .orElseThrow(() -> new BadRequestException(
                        "Cannot found subject with ID: " + teacherRegister.getSubjectId()));

        teacherRepository.save(Teacher
                .builder()
                .information(userInformation)
                .subject(subject)
                .build());
    }

     @Override
    public void studentRegister(StudentRegister studentRegister) {
        UserRegister newUserInformation = studentRegister.getUserRegister();
        newUserInformation.setRole(Role.STUDENT);
        UserInformation userInformation = userRegister(newUserInformation);
        
        throw new UnsupportedOperationException("Unimplemented method 'studentRegister'");
    }

    @Transactional
    private UserInformation userRegister(UserRegister userRegister) {

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

        return userInformationRepository.save(newUser);
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
