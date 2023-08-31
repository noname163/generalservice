package com.cepa.generalservice.services.authenticationService;

import com.cepa.generalservice.data.dto.request.LoginRequest;
import com.cepa.generalservice.data.dto.response.LoginResponse;

public interface AuthenticationService {
    public LoginResponse login(LoginRequest loginRequest);
}
