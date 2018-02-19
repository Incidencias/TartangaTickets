package com.tartangatickets.utils;

import com.tartangatickets.utils.exceptions.EncrypterException;
import java.util.List;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true;

    /*
    public static void main(String[] args) {
        EmailSender sender = new EmailSender();
        sender.sendEmail();
    }
    */

    public static void sendEmail(String toAddress) 
            throws EncrypterException, EmailException {
        String subject = "Prueba Email";
        String message = "Que pasa ninio cebolleta";
        List<String> emailCredentials = Encrypter.decryptFile();
        String userName = emailCredentials.get(0);
        String password = emailCredentials.get(1);
        Email email = new SimpleEmail();
        email.setHostName(HOST);
        email.setSmtpPort(PORT);
        email.setAuthenticator(new DefaultAuthenticator(userName, password));
        email.setSSLOnConnect(SSL_FLAG);
        email.setFrom(userName);
        email.setSubject(subject);
        email.setMsg(message);
        email.addTo(toAddress);
        email.send();
    }
    
    public static void sendEmail(String toAddress, String newPassword) 
            throws EncrypterException, EmailException {
        String subject = "Nuevo usuario";
        String message = "Nuevo usuario: "+ toAddress
                + "\nConstraseña: " + newPassword;
        List<String> emailCredentials = Encrypter.decryptFile();
        String userName = emailCredentials.get(0);
        String password = emailCredentials.get(1);
        Email email = new SimpleEmail();
        email.setHostName(HOST);
        email.setSmtpPort(PORT);
        email.setAuthenticator(new DefaultAuthenticator(userName, password));
        email.setSSLOnConnect(SSL_FLAG);
        email.setFrom(userName);
        email.setSubject(subject);
        email.setMsg(message);
        email.addTo(toAddress);
        email.send();
    }
}
