package com.cepa.generalservice.services.userService.impl;

import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
import java.net.http.HttpClient.Redirect;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cepa.generalservice.controllers.RedirectController;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.EnvironmentVariables;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.userService.UserService;
>>>>>>> fc86c36d34e788d6325f7bbdf3c4784da51007af

import lombok.Builder;

@Service
@Builder
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInformationRepository userInformationRepository;

    @Override
    public UserInformation getUserByEmail(String email) {
        return userInformationRepository
                .findByEmailAndStatus(email, UserStatus.ENABLE)
                .orElseThrow(() -> new BadRequestException("Email " + email + " is not exist"));
    }

}
