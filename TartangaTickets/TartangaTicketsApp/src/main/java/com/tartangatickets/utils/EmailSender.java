package com.tartangatickets.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class EmailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true;
    private static final String USER_NAME = "incidencias.tartanga@gmail.com";
    private static final String PASSW = "equipoa2018";

    /*
    public static void main(String[] args) {
        EmailSender sender = new EmailSender();
        sender.sendSimpleEmail();
    }
    */

    public static void sendSimpleEmail(String toAddress) {
        
        String password = "equipoa2018";
        
        String subject = "Prueba Email";
        String message = "Que pasa ninio cebolleta";
        
        try {
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(USER_NAME, password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(USER_NAME);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(toAddress);
            email.send();
        }catch(Exception ex){
            System.out.println("Unable to send email");
            System.out.println(ex);
        }
    }
}
