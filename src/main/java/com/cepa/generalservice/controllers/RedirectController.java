package com.cepa.generalservice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/redirect")
    public void redirectToValidateSuccess(HttpServletResponse response) {
        try {
            response.sendRedirect("https://capstone-ibc2bij0w-dat-nguyen-304.vercel.app/auth/123");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 
}
