package com.cepa.generalservice.services.userService.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.UserRequest;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
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
    public UserResponse updateUserById(Long id, UserRequest userRequest) {
        UserInformation userExist = userInformationRepository.findByIdAndStatus(id, UserStatus.ENABLE)
                .orElseThrow(() -> new BadRequestException("User not found with id: " + id));

        userExist.setFullName(userRequest.getFullName());
        userExist.setImageURL(userRequest.getUrl());
        System.out.println(userRequest.getFullName());
        System.out.println(userRequest.getUrl());
        System.out.println(userRequest.getDateOfBirth());
        if (userRequest.getDateOfBirth() != null) {
            userExist.setDateOfBirth(userRequest.getDateOfBirth());
        } else {
            userExist.setDateOfBirth(null); // Handle null value if needed
        }
        userExist = userInformationRepository.save(userExist);

        return userInformationMapper.mapEntityToDto(userExist);
    }

}
