package com.tartangatickets.utils;

import com.tartangatickets.utils.exceptions.EncrypterException;
import java.util.List;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Sends emails from an email account
 *  
 *  <ul>
 *      <li><strong>HOST:</strong> The smtp used to send emails</li>
 *      <li><strong>PORT:</strong> Used port</li>
 *      <li><strong>SSL_FLAG:</strong> if uses ssl </li>
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
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
    /*
         example
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
    */
    /**
     * Sends email with a password to a given address 
     * @param toAddress String - mail of the email receiver
     * @param newPassword String - the password, is the message of the email
     * @throws EncrypterException
     * @throws EmailException 
     */
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
