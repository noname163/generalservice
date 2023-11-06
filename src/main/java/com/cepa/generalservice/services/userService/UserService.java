package com.cepa.generalservice.services.userService;

import com.cepa.generalservice.data.dto.request.ChangePasswordRequest;
import com.cepa.generalservice.data.dto.request.ForgotPassword;
import com.cepa.generalservice.data.dto.request.UserRequest;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;

public interface UserService {
    public UserInformation getUserByEmail(String email);

    public UserInformation getUserByEmailIgnorStatus(String email);

    public UserResponse getUserResponseByEmail(String email);

    public void forgotPassword(ForgotPassword forgotPassword);

    public void changePassword(ChangePasswordRequest changePasswordRequest);

    public Boolean userConfirmEmail(String token);

    public void userActivateAccount(String token);

    public UserResponse updateUserByEmail(String email, UserRequest userRequest);

}
