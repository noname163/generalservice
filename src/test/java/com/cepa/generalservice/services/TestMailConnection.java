package com.cepa.generalservice.services;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.junit.jupiter.api.Test;

public class TestMailConnection {

    @Test
    public void testMailConnection() {
        // Your mail connection code here
        
        final String username = "akai792001@gmail.com";
        final String password = "mjihqbluklrhpxkm";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            System.out.println("Connected successfully");
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
