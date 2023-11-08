package com.cepa.generalservice.services.authenticationService;

import javax.servlet.http.HttpServletRequest;

import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.request.TokenRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;

public interface AuthenticationService {
    public LoginResponse login(LoginRequest loginRequest);

    public LoginResponse reFreshToken(String refreshToken);

    public LoginResponse reFreshToken(HttpServletRequest request);

    public LoginResponse loginWithGoogle(TokenRequest token);

    public void logout(String email);
}
