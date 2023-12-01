package com.cepa.generalservice.services.userService.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.BanRequest;
import com.cepa.generalservice.data.dto.request.ChangePasswordRequest;
import com.cepa.generalservice.data.dto.request.EditUserRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.SendMailRequest;
import com.cepa.generalservice.data.dto.request.UserRequest;
import com.cepa.generalservice.data.dto.response.AdminEditUserStatus;
import com.cepa.generalservice.data.dto.response.CloudinaryUrl;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.InValidInformation;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;
import com.cepa.generalservice.services.uploadservice.UploadService;
import com.cepa.generalservice.services.userService.UserService;

import lombok.Builder;

@Service
@Builder
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private UserInformationMapper userInformationMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public UserInformation getUserByEmail(String email) {
        return userInformationRepository
                .findByEmailAndStatus(email, UserStatus.ENABLE)
                .orElseThrow(() -> new BadRequestException("Email " + email + " is not exist"));
    }

    @Override
    public void forgotPassword(ForgotPassword forgotPassword) {

        if (!forgotPassword.getConfirmPassword().equals(forgotPassword.getPassword())) {
            throw new BadRequestException("Password did not match");
        }
        confirmTokenService.verifyToken(forgotPassword.getUuid());
        UserInformation userInformation = confirmTokenService.getUserByToken(forgotPassword.getUuid());

        userInformation.setPassword(passwordEncoder.encode(forgotPassword.getPassword()));
        userInformationRepository.save(userInformation);
    }

    @Override
    public Boolean userConfirmEmail(String token) {
        return confirmTokenService.verifyToken(token);
    }

    @Override
    public void userActivateAccount(String token) {
        Boolean confirmStatus = confirmTokenService.verifyToken(token);
        if (Boolean.TRUE.equals(confirmStatus)) {
            UserInformation userInformation = confirmTokenService.getUserByToken(token);
            userInformation.setStatus(UserStatus.ENABLE);
            userInformationRepository.save(userInformation);
        }
        confirmTokenService.verifyToken(token);
    }

    @Override
    public UserInformation getUserByEmailIgnorStatus(String email) {
        return userInformationRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email " + email + " is not exist"));
    }

    @Override
    public UserResponse getUserResponseByEmail(String email) {
        UserInformation userInformation = getUserByEmail(email);
        return userInformationMapper.mapEntityToDto(userInformation);
    }

    @Override
    public UserResponse updateUserByEmail(String email, UserRequest userRequest) {
        UserInformation userExist = userInformationRepository.findByEmailAndStatus(email, UserStatus.ENABLE)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));

        userExist.setFullName(userRequest.getFullName());
        userExist.setImageURL(userRequest.getUrl());
        if (userRequest.getDateOfBirth() != null) {
            userExist.setDateOfBirth(userRequest.getDateOfBirth());
        } else {
            userExist.setDateOfBirth(null); // Handle null value if needed
        }
        userExist = userInformationRepository.save(userExist);

        return userInformationMapper.mapEntityToDto(userExist);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {

        UserInformation userExist = securityContextService.getCurrentUser();

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userExist.getPassword())) {
            throw new InValidInformation("Password is incorrect. Please try again");
        }
        if (!changePasswordRequest.getConfirmPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new BadRequestException("Password did not match");
        }
        userExist.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userInformationRepository.save(userExist);

    }

    @Override
    public void editUserStatus(AdminEditUserStatus editUserStatus) {
        UserInformation userInformation = userInformationRepository
                .findById(editUserStatus.getUserId())
                .orElseThrow(() -> new BadRequestException("Cannot found user with id " + editUserStatus.getUserId()));
        if (userInformation.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("Cannot change admin account");
        }
        if (userInformation.getStatus().equals(editUserStatus.getUserStatus())) {
            throw new BadRequestException("There is no difference to change");
        }
        userInformation.setStatus(editUserStatus.getUserStatus());
        userInformation.setUpdateDate(LocalDateTime.now());
        userInformationRepository.save(userInformation);
    }

    @Override
    public void editUserInformation(EditUserRequest editUserRequest, MultipartFile multipartFile) {
        UserInformation currentUser = securityContextService.getCurrentUser();
        if (multipartFile != null) {
            CloudinaryUrl cloudinaryUrl = uploadService.uploadMedia(multipartFile);
            currentUser.setImageURL(cloudinaryUrl.getUrl());
            currentUser.setCloudPublicId(cloudinaryUrl.getPublicId());
        }

        currentUser.setDateOfBirth(
                Optional.ofNullable(editUserRequest.getDateOfBirth()).orElse(currentUser.getDateOfBirth()));
        currentUser.setFullName(Optional.ofNullable(editUserRequest.getFullName()).orElse(currentUser.getFullName()));
        currentUser.setDescription(
                Optional.ofNullable(editUserRequest.getDesciption()).orElse(currentUser.getDescription()));
        currentUser.setUpdateDate(LocalDateTime.now());
        userInformationRepository.save(currentUser);
    }

    @Override
    public void banUser(BanRequest banRequest) {
        UserInformation userInformation = userInformationRepository
                .findByIdAndStatusNot(banRequest.getAccountId(), UserStatus.BANNED)
                .orElseThrow(() -> new BadRequestException("Not exist user with id " + banRequest.getAccountId()));
        userInformation.setStatus(UserStatus.BANNED);
        userInformationRepository.save(userInformation);
        sendEmailService.sendMailService(SendMailRequest
                .builder()
                .subject("Thông báo hệ thống")
                .mailTemplate(VerificationTokenTemplate.banEmail(userInformation.getFullName(), banRequest.getReason()))
                .userEmail(userInformation.getEmail())
                .build());
    }

}
