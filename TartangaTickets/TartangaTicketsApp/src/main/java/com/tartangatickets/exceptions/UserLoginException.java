/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.exceptions;

/**
 *
 * @author ubuntu
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
