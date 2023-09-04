package com.cepa.generalservice.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class TestMailConnection {
    public static void main(String[] args) {
        final String username = "akai792001@gmail.com";
        final String password = "mjihqbluklrhpxkm";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
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
