package com.cepa.generalservice.services.userService.impl;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.controllers.RedirectController;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
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
    private RedirectController redirect;
    @Autowired
    private HttpServletResponse response;

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

        userInformation.setPassword(forgotPassword.getPassword());
        userInformationRepository.save(userInformation);
    }

    @Override
    public void userConfirmEmail(String token, String from) {
        UserInformation userInformation = confirmTokenService.getUserByToken(token);
        ConfirmToken userToken = confirmTokenService.getTokenByEmail(userInformation.getEmail());

        if (!userToken.getToken().toString().equals(token)) {
            throw new BadRequestException("Token not valid");
        }

        Boolean confirmStatus = confirmTokenService.verifyToken(token);

        if (Boolean.TRUE.equals(confirmStatus)) {
            if (from.equals("register")) {
                userInformation.setStatus(UserStatus.ENABLE);
                userInformationRepository.save(userInformation);
                redirect.redirectToValidateSuccess(response);
            }
            if (from.equals("forgot-password")) {
                redirect.rediectToResetPassword(response, userToken.getToken().toString());
            }
        }

    }

}
