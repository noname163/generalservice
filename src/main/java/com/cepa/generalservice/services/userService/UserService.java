package com.cepa.generalservice.services.userService;

import com.cepa.generalservice.data.entities.UserInformation;

public interface UserService {
    public UserInformation getUserByEmail(String email);
}
