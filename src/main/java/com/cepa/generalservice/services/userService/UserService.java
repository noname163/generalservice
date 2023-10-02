package com.cepa.generalservice.services.userService;

import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.entities.UserInformation;

public interface UserService {
    public UserInformation getUserByEmail(String email);
    public void forgotPassword(ForgotPassword forgotPassword);
    public void userConfirmEmail(String token, String from);
}
