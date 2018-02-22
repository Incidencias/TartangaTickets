/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.exceptions;

/**
 *
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class UserLoginException extends Exception {

    /**
     * Creates a new instance of <code>UserLoginException</code> without detail
     * message.
     */
    public UserLoginException() {
    }

    /**
     * Constructs an instance of <code>UserLoginException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserLoginException(String msg) {
        super(msg);
    }
}
