package com.cepa.generalservice.services.authenticationService;

import com.cepa.generalservice.data.entities.UserInformation;

public interface SecurityContextService {
    public void setSecurityContext(String email);

    public void validateCurrentUser(UserInformation user);

    public UserInformation getCurrentUser();
}
