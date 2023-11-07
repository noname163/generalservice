package com.cepa.generalservice.services.userService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.ChangePasswordRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.UserRequest;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.InValidInformation;
import com.cepa.generalservice.exceptions.UserNotExistException;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
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
        UserInformation userExist = userInformationRepository
                .findByEmailAndStatus(changePasswordRequest.getEmail(), UserStatus.ENABLE)
                .orElseThrow(() -> new UserNotExistException(
                        "User not found with email: " + changePasswordRequest.getEmail()));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userExist.getPassword())) {
            throw new InValidInformation("Password is incorrect. Please try again");
        }
        if (!changePasswordRequest.getConfirmPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new BadRequestException("Password did not match");
        }
        userExist.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userInformationRepository.save(userExist);

    }

}
