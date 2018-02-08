/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils.exceptions;

/**
 *
 * @author ubuntu
 */
public class EncrypterException extends Exception {

    /**
     * Creates a new instance of <code>EncrypterException</code> without detail
     * message.
     */
    public EncrypterException() {
    }

    /**
     * Constructs an instance of <code>EncrypterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncrypterException(String msg) {
        super(msg);
    }
    
    public EncrypterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
