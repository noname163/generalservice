package com.cepa.generalservice.services.authenticationService.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.InValidAuthorizationException;
import com.cepa.generalservice.exceptions.InValidInformation;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.exceptions.UserNotExistException;
import com.cepa.generalservice.services.authenticationService.AuthenticationService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.CookiesUtil;
import com.cepa.generalservice.utils.JwtTokenUtil;

import io.jsonwebtoken.Claims;
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
    @Autowired
    private UserService userService;
    @Autowired
    private CookiesUtil cookiesUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserInformation userInformation = userInformationRepository
                .findByEmailAndStatus(loginRequest.getEmail(), UserStatus.ENABLE)
                .orElseThrow(() -> new UserNotExistException("User not exist."));
        if (!passwordEncoder.matches(loginRequest.getPassword(), userInformation.getPassword())) {
            throw new InValidInformation("Username or password is incorrect. Please try again");
        }

        return generateLoginResponseByUser(userInformation);
    }

    @Override
    public LoginResponse reFreshToken(String refreshToken) {
        Claims claims = jwtTokenUtil.verifyRefreshToken(refreshToken);
        String email = jwtTokenUtil.getEmailFromClaims(claims);
        UserInformation userInformation = userInformationRepository.findByEmailAndStatus(email, UserStatus.ENABLE)
                .orElseThrow(() -> new UserNotExistException("User not exist."));

        return generateLoginResponseByUser(userInformation);
    }

    private LoginResponse generateLoginResponseByUser(UserInformation userInformation) {

        String accessToken = jwtTokenUtil.generateJwtToken(userInformation, 1000);
        String refreshToken = jwtTokenUtil.generateJwtToken(userInformation, 10000);
        userInformation.setAccessToken(accessToken);
        userInformation.setRefreshToken(refreshToken);
        userInformationRepository.save(userInformation);

        return LoginResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String email) {
        UserInformation userInformation = userService.getUserByEmail(email);

        userInformation.setAccessToken("");
        userInformation.setRefreshToken("");
        userInformationRepository.save(userInformation);
    }

    @Override
    public LoginResponse reFreshToken(HttpServletRequest request) {
        String refreshToken = cookiesUtil.getCookieValue(request, "refresh-token");
        if(refreshToken==null){
            throw new NotFoundException("Refresh token not found in cookies");
        }

        Claims claims = jwtTokenUtil.verifyRefreshToken(refreshToken);
        String email = jwtTokenUtil.getEmailFromClaims(claims);
        UserInformation userInformation = userInformationRepository.findByEmailAndStatus(email, UserStatus.ENABLE)
                .orElseThrow(() -> new UserNotExistException("User not exist."));
        
        return generateLoginResponseByUser(userInformation);
        
    }
}
