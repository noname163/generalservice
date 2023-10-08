package com.cepa.generalservice.services.authenticationService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.utils.JwtTokenUtil;

import lombok.Builder;

@Service
@Builder
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserInformation userInformation = userInformationRepository
                .findByEmailAndStatus(loginRequest.getEmail(), UserStatus.ENABLE)
                .orElseThrow(() -> new BadRequestException("User not exist."));
        if (!passwordEncoder.matches(loginRequest.getPassword(), userInformation.getPassword())) {
            throw new BadRequestException("Username or password is incorrect. Please try again");
        }
        String accessToken = jwtTokenUtil.generateJwtToken(userInformation, 1000);
        String refreshToken = jwtTokenUtil.generateJwtToken(userInformation, 10000);
        return LoginResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
