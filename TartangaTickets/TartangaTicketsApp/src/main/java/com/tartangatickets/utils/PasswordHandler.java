/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.binary.Hex;

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
    
    public static String getHash(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md;
        String saltedPassword = salt + password;
        byte[] passwordBytes = saltedPassword.getBytes();
        md = MessageDigest.getInstance(SHA_512);
        return Arrays.toString(Hex.encodeHex(md.digest(passwordBytes)));
    }
    
    public static boolean checkSecurity(String passwrod) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(passwrod);
        return matcher.matches();
    }
    
    
    public static String generatePassword() {
        return RandomStringUtils.random(8, 0, CHARACTERS.length-1, 
                false, false, CHARACTERS, new SecureRandom());
    }
}
