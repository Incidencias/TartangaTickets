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
public class NoDepartmentException extends Exception {

    /**
     * Creates a new instance of <code>NoDepartmentException</code> without
     * detail message.
     */
    public NoDepartmentException() {
    }

    /**
     * Constructs an instance of <code>NoDepartmentException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoDepartmentException(String msg) {
        super(msg);
    }
}
