package com.cepa.generalservice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class RedirectController {
    public void redirectToValidateSuccess(HttpServletResponse response){
        try {
            response.sendRedirect("https://capstone-ibc2bij0w-dat-nguyen-304.vercel.app/auth/123");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void rediectToResetPassword(HttpServletResponse response, String uuid){
        try {
            response.sendRedirect("https://capstone-ibc2bij0w-dat-nguyen-304.vercel.app/auth/123/"+uuid);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
