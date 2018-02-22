/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check if a given email format is valid
 *  
 *  <ul>
 *      <li><strong>EMAIL_REGEX:</strong> Regex to check the email security</li>

 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class Reader {
    
    private static final String EMAIL_REGEX = 
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:"
            + "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    
    /**
     *  Check if a given email format is valid
     * @param email String email to check
     * @return boolean True - pass filter/False - don't pass filter
     */
    public static boolean checkValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
