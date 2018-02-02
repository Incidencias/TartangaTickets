package com.tartangatickets.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class SendEmail {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true; 

    public static void main(String[] args) {
        SendEmail sender = new SendEmail();
        sender.sendSimpleEmail();
    }

    private void sendSimpleEmail() {
        
        String userName = "incidencias.tartanga@gmail.com";
        String password = "equipoa2018";
        
        String fromAddress="incidencias.tartanga@gmail.com";
//        String toAddress ="Miguelaxierlp@gmail.com";
        String toAddress ="zzz@gmail.com";
        String subject = "Prueba Email";
        String message = "Que pasa ninio cebolleta";
        
        try {
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
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
