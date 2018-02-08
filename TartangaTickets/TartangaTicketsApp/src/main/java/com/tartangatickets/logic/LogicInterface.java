package com.tartangatickets.logic;

import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.State;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ubuntu
 */
public interface LogicInterface {
    
    public HashMap<String, String> getSessionContent();
    public void createTicket(Ticket ticket) throws Exception; //Check
    public void sendMessage(Message message) throws Exception; //Check
    public List<Ticket> findTicketsByUser(String userLogin) throws Exception; //Check
    public List<Ticket> findAllTickets() throws Exception; //Check
    public void changePassword(Credential credential, String newPassword) throws Exception; //Check
    public void recoverPassword(String login) throws Exception; //Check
    public User createUser(User user) throws Exception; //Check
    public void deleteUser(User user) throws Exception; //Check
    /*
    public void createTechnician(Technician technician) throws Exception;
    public void deleteTechnician(Technician technician) throws Exception;
    public void updateTechnician(Technician technician) throws Exception;
    */
    public List<User> findAllUsers() throws Exception; //Check
    public void assignTicket(Ticket ticket) throws Exception; //Check
    public void changeState(Ticket ticket) throws Exception; //Check
    public User authenticate(String login, String password) throws Exception; //Check
    public List<Department> findAllDepartments() throws Exception; //Check
    public List<Ticket> findTicketsByState(State state) throws Exception; //Check
    public List<Ticket> findTicketsByTechnician(String technicianLogin) throws Exception;
}
