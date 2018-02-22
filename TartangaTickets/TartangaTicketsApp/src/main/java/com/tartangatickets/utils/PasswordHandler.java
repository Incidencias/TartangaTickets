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
 * Handle the password operations, checking the password security and generate secure password
 *  
 *  <ul>
 *      <li><strong>SHA_512:</strong> Security algorithm</li>
 *      <li><strong>PASSWORD_REGEX:</strong> Password regex to check password security</li>
 *      <li><strong>CHARACTERS:</strong> characters for password creation </li>
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class PasswordHandler {
    
    private static final String SHA_512 = "SHA-512";
    private static final String PASSWORD_REGEX = 
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final char[] CHARACTERS = 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+="
                    .toCharArray();
    
    /**
     * Get hash code using a password and salt
     * @param password String password 
     * @param salt String salt to add to the password
     * @return String - the generated hash
     * @throws NoSuchAlgorithmException 
     */
    public static String getHash(String password, String salt) 
            throws NoSuchAlgorithmException {
        MessageDigest md;
        String saltedPassword = salt + password;
        byte[] passwordBytes = saltedPassword.getBytes();
        md = MessageDigest.getInstance(SHA_512);
        return Arrays.toString(Hex.encodeHex(md.digest(passwordBytes)));
    }
    
    /**
     * Checks complexity/security of a given password
     * @param password String- password to check
     * @return boolean True - security control pass /False - not secure enough
     */
    public static boolean checkSecurity(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    /**
     * Generates a passwords
     * @return String - generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.random(8, 0, CHARACTERS.length-1, 
                false, false, CHARACTERS, new SecureRandom());
    }
}
