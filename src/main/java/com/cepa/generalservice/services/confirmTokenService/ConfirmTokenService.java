package com.cepa.generalservice.services.confirmTokenService;

import java.util.UUID;

import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;

public interface ConfirmTokenService {
    public UUID saveConfirmToken(String email);
    public Boolean verifyToken(String provideToken);
    public UserInformation getUserByToken(String token);
    public ConfirmToken getTokenByEmail(String email);
    public UUID reCreateToken(ConfirmToken confirmToken);
}
