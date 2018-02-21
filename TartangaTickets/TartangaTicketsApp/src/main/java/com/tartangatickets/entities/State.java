/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.entities;

/**
 *
 * @author ubuntu
 */
public class State {

    public enum STATE {

        OPEN,
        INPROGRESS,
        BLOQUED,
        CLOSED;

    }
    
    public static STATE getState(Ticket ticket) {
        return ticket.getState();
    }
}
