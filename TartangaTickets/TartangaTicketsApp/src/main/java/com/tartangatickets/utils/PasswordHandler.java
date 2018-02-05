/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author ubuntu
 */
public class PasswordHandler {
    
    private static final String SHA_512 = "SHA-512";
    private static final String PASSWORD_REGEX = 
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final char[] CHARACTERS = 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=".toCharArray();
    
    public static String getHash(String password, String salt) throws Exception {
        MessageDigest md;
        String saltedPassword = salt + password;
        byte[] passwordBytes = saltedPassword.getBytes();
        StringBuilder sb;
        try {
            md = MessageDigest.getInstance(SHA_512);
            md.update(passwordBytes);
            sb = new StringBuilder();
            for (byte theByte : md.digest()) 
                sb.append(String.format("%02x", theByte & 0xff));          
        } catch (NoSuchAlgorithmException e) {
            throw new Exception();
        }
        return sb.toString();
    }
    
    public static boolean checkSecurity(String passwrod) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(passwrod);
        return matcher.matches();
    }
    
    
    public String generatePassword() {
        return RandomStringUtils.random(8, 0, CHARACTERS.length-1, 
                false, false, CHARACTERS, new SecureRandom());
    }
    
    // TODO Decipher configuration file
}
