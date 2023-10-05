package com.cepa.generalservice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cepa.generalservice.utils.EnvironmentVariables;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class RedirectController {

    @Autowired
    private EnvironmentVariables environmentVariables;
    
    public void redirectToValidateSuccess(HttpServletResponse response, String token){
        try {
            response.sendRedirect(environmentVariables.getRegisterUI()+token);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void rediectToResetPassword(HttpServletResponse response, String uuid){
        try {
            response.sendRedirect(environmentVariables.getForgotUI()+uuid);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
