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
    public void createTicket(Ticket ticket) throws Exception;
    public void sendMessage(Message message) throws Exception;
    public List<Ticket> findTicketsByUser(String userLogin) throws Exception;
    public List<Ticket> findAllTickets() throws Exception;
    public void changePassword(Credential credential, String newPassword) throws Exception;
    public void recoverPassword(String login) throws Exception;
    public void createUser(User user) throws Exception;
    public void deleteUser(User user) throws Exception;
    /*
    public void createTechnician(Technician technician) throws Exception;
    public void deleteTechnician(Technician technician) throws Exception;
    public void updateTechnician(Technician technician) throws Exception;
    */
    public List<User> findAllUsers() throws Exception;
    public void assignTicket(Ticket ticket) throws Exception;
    public void changeState(Ticket ticket) throws Exception;
    public User authenticate(String login, String password) throws Exception;
    public List<Department> findAllDepartments() throws Exception;
    public List<Ticket> findTicketsByState(State state) throws Exception;
    public List<Ticket> findTicketsByTechnician(String technicianLogin) throws Exception;
}
