/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.logic;

import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import java.util.List;

/**
 *
 * @author ubuntu
 */
public interface LogicInterface {
    
    public void createTicket(Ticket ticket) throws Exception;
    public void sendMessage(Message message) throws Exception;
    public List<Ticket> findTicketsByUser(Integer userId) throws Exception;
    public List<Ticket> findAllTickets() throws Exception;
    public void changePassword(Credential credential, String newPassword) throws Exception;
    public void recoverPassword(Integer userId) throws Exception;
    public void createUser(User user) throws Exception;
    public void deleteUser(User user) throws Exception;
    public List<User> findAllUsers() throws Exception;
    public void assignTicket(Ticket ticket) throws Exception;
    public void changeState(Ticket ticket) throws Exception;
    public void authenticate(String login, String password) throws Exception;
       
}
