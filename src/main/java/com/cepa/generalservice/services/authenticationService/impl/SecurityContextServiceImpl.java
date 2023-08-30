package com.cepa.generalservice.services.authenticationService.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.configs.CustomUserDetails;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.ForbiddenException;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;


@Service
public class SecurityContextServiceImpl implements SecurityContextService {
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private UserInformationRepository userRepository;

    @Override
    public void setSecurityContext(String email) {
        Optional<UserInformation> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ForbiddenException("Invalid username in JWT.");
        }
        UserDetails userDetails = new CustomUserDetails(userOptional.get());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public UserInformation getCurrentUser() {
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        return ((CustomUserDetails) principal).getUser();
    }

    @Override
    public void validateCurrentUser(UserInformation user) {
        UserInformation currentUser = getCurrentUser();
        if(!currentUser.getEmail().equals(user.getEmail())){
            throw new BadRequestException("Invalid User");
        }
    }
}
