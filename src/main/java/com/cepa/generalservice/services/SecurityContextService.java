package com.cepa.generalservice.services;

import com.cepa.generalservice.data.entities.UserInformation;

public interface SecurityContextService {
    public void setSecurityContext(String username);

    public void validateCurrentUser(UserInformation user);

    public UserInformation getCurrentUser();
}
