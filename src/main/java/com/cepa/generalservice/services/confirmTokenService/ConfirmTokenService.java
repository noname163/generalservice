package com.cepa.generalservice.services.confirmTokenService;

import java.util.UUID;

import com.cepa.generalservice.data.entities.UserInformation;

public interface ConfirmTokenService {
    public UUID saveConfirmToken(UserInformation userInformation);
    public Boolean verifyToken(String provideToken);
}
