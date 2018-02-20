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
public class NoTechnicianException extends Exception {

    /**
     * Creates a new instance of <code>NoTechnicianException</code> without
     * detail message.
     */
    public NoTechnicianException() {
    }

    /**
     * Constructs an instance of <code>NoTechnicianException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoTechnicianException(String msg) {
        super(msg);
    }
}
