/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.logic;

import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoDepartmentException;
import com.tartangatickets.exceptions.NoTechnicianException;
import com.tartangatickets.exceptions.NoTicketException;
import com.tartangatickets.exceptions.NoUserException;
import com.tartangatickets.exceptions.UserLoginException;
import com.tartangatickets.utils.exceptions.NotSecureException;
import com.tartangatickets.utils.EmailSender;
import com.tartangatickets.utils.HibernateUtil;
import com.tartangatickets.utils.PasswordHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.logging.Level;
import javax.transaction.Transactional;

/**
 * Implements all the logic transactions of the program
 * 
 *  
 *  <ul>
 *      <li><strong>LOGGER:</strong> Logger.</li>
 *      <li><strong>factory:</strong> Factory of the SessionFactory.</li>
 *      <li><strong>session:</strong> Open session of factory</li>
 *      <li><strong>tx:</strong> Transaction</li>
 *      <li><strong>SESSION_CONTENT:</strong>HashMap to move data inside the application </li>
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */

@Transactional(rollbackOn = Exception.class)
public class Logic implements LogicInterface {
    
    private static final Logger LOGGER = Logger.getLogger("com.tartangatickets.logic");
    private static final String TICKET_STATE_CHANGE = "TicketStateChange";
    private static final String TICKET_UPDATE_TECHNICIAN = "TicketUpdateTechnician";
    private static final String USER_TICKET_UPDATE_TECHNICIAN = "UserTicketUpdateTechnician";
    private static final String TICKET_CREATED = "TicketCreated";
    private final SessionFactory factory = HibernateUtil.getSessionFactory();
    private final Session session = factory.openSession();
    private Transaction tx = null;
    private static final HashMap SESSION_CONTENT = new HashMap<>();
    
    /**
     * Finds a ticket using a id of the ticket
     * @param id identificator of the ticket
     * @return corresponding ticket to the id
     * @throws java.lang.Exception
     */
    @Override
    public Ticket findTicketById(Integer id) throws Exception {
        LOGGER.info("Fetching ticket by Id");
        tx = session.beginTransaction();
        List<Ticket> tickets = session
                .createNamedQuery("findTicketById")
                .setParameter("id", id)
                .getResultList();
        if (tickets == null || tickets.isEmpty()) {
            tx.rollback();
            throw new NoTicketException("No se encntro la incidencia");
        }
        tx.commit();
        LOGGER.info("Ticket found");
        return tickets.get(0);
    }
    /**
     * 
     * @return the session_content HashMap
     */
    @Override
    public HashMap getSessionContent() {
        return SESSION_CONTENT;
    }
    
    /**
     * Creates a new ticket with received parameters
     * @param ticket 
     * @throws java.lang.Exception 
     */
    @Override
    public void createTicket(Ticket ticket) throws Exception {
        LOGGER.info("Creating ticket");
        tx = session.beginTransaction();
        ticket.setCreateDate(new Date());
        session.persist(ticket);
        session.flush();
        session.refresh(ticket);
        User user = ticket.getUser();
        List<Ticket> userTickets = user.getCreatedTickets();
        if (userTickets == null)
            userTickets = new ArrayList<>();
        userTickets.add(ticket);
        user.setCreatedTickets(userTickets);
        session.merge(user);
        session.flush();
        session.refresh(user);
        tx.commit();
        LOGGER.info("Ticket created");
        LOGGER.info("Sending created ticket notification");
        sendNotification(ticket, TICKET_CREATED);
        
    }

//    /**
//     * Appends new message a ticket
//     * @param message 
//     * @throws java.lang.Exception 
//     */
//    @Override
//    public void sendMessage(Message message) throws Exception {
//        LOGGER.info("Creating ticket message");
//        tx = session.beginTransaction();
//        //session.persist(message);
//        Ticket ticket = message.getTicket();
//        List<Message> messages = new ArrayList<>();
//        messages.add(message);
//        ticket.setMessages(messages);
//        session.merge(ticket);
//        tx.commit();
//        LOGGER.info("Ticket message created");
//    }

    /**
     * Finds a ticked using the user login
     * @param userLogin user identification (email)
     * @return list of tickets corresponding to received user
     * @throws java.lang.Exception
     */
    @Override
    public List<Ticket> findTicketsByUser(String userLogin) 
            throws Exception {
        LOGGER.info("Fetching tickets by user");
        List<Ticket> tickets = null;
        tx = session.beginTransaction();
        tickets = session.createNamedQuery("findTicketsByUser")
                .setParameter("login", userLogin)
                .getResultList();
        if (tickets == null || tickets.isEmpty()){
            tx.rollback();
            throw new NoTicketException("No se encontraron tickets");
        }
            
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                tickets.size());
        tx.commit();
        return tickets;
    }

    /**
     * Finds all the tickets data
     * @return List with all tickets
     * @throws Exception 
     */
    @Override
    public List<Ticket> findAllTickets() throws Exception {
        LOGGER.info("Fetching all tickets");
        List<Ticket> tickets = null;
        tx = session.beginTransaction();
        tickets = session.createNamedQuery("findAllTickets")
                .getResultList();
        
        if (tickets == null || tickets.isEmpty()){
           tx.rollback();
           throw new NoTicketException("No se encontraron tickets");
        }
          
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                tickets.size());
        tx.commit();
        return tickets;
    }
    
    /**
     * Change a password from the user corresponding to credential
     * @param credential accreditation of the user
     * @param newPassword new password of the user
     * @throws Exception 
     */
    @Override
    public void changePassword(Credential credential, String newPassword) 
            throws Exception {
        LOGGER.info("Changing user password");
        if (PasswordHandler.checkSecurity(newPassword)) {
            tx = session.beginTransaction();
            String passwordHash = PasswordHandler
                    .getHash(newPassword, credential.getLogin());
            credential.setPassword(passwordHash);
            session.merge(credential);
            
        } else {
            tx.rollback();
            LOGGER.warning("Password not secure");
            throw new NotSecureException("Contraseña debe tener mayúscula, "
                    + "minúscula, carácter especial y dígito");
        }
        tx.commit();
        LOGGER.info("Password changed");
    }

    /**
     * Recovers the received user password, sends an email with new password
     * @param login user email
     * @throws Exception 
     */
    @Override
    public void recoverPassword(String login) throws Exception {
        LOGGER.info("Recovering user password");
        tx = session.beginTransaction();
        List<User> users = session.createNamedQuery("findUserById")
            .setParameter("login", login)
            .getResultList();
        if (users == null || users.isEmpty()){
            tx.rollback();
            throw new NoUserException("El usuario no existe");
        }
            
        String newPassword = setPassword(users.get(0));
        session.merge(users.get(0));
        LOGGER.info("Sending email");
        EmailSender.sendEmail(login, newPassword);
        tx.commit();
        LOGGER.info("Password recovery successful");
    }
    
    /**
     * Sets a password to a specific user
     * @param user a user to change password
     * @return the new password
     * @throws Exception 
     */
    private String setPassword(User user) throws Exception  {
        LOGGER.info("Setting new password");
        String login = user.getCredential().getLogin();
        String newPassword = PasswordHandler.generatePassword();
        LOGGER.info("Setting user credentials");
        String passwordHash = PasswordHandler.getHash(newPassword, login);
        user.getCredential().setPassword(passwordHash);
        user.getCredential().setLastPassChange(new Date());
        return newPassword;
    }

    /**
     * Creates a user with the given data and sends an email with the generated
     * password to the user email
     * @param user the new user 
     * @return the created user
     * @throws Exception 
     */
    @Override
    public User createUser(User user) throws Exception {
        LOGGER.info("Creating user");
        tx = session.beginTransaction();
        String newPassword = setPassword(user);
        session.persist(user);
        LOGGER.info("Sending email");
        EmailSender.sendEmail(user.getCredential().getLogin(), newPassword);
        tx.commit();
        LOGGER.info("User created");
        return user;
    }

//    /**
//     * Delete user from database
//     * @param user a user to erase
//     * @throws java.lang.Exception
//     */
//    @Override
//    public void deleteUser(User user) throws Exception {
//        LOGGER.info("Deleting user");
//        tx = session.beginTransaction();
//        session.remove(user);
//        tx.commit();
//        LOGGER.info("User deleted");
//    }

    /**
     * Finds all users
     * @return a list of all the users
     * @throws Exception 
     */
    @Override
    public List<User> findAllUsers() throws Exception {
        LOGGER.info("Fetching all users");
        List<User> users = null;
        tx = session.beginTransaction();
        users = session.createNamedQuery("findAllUsers")
                .getResultList();
        if (users == null || users.isEmpty()){
            tx.rollback();
            throw new NoUserException("No users found");
        }
            
        tx.commit();
        LOGGER.log(Level.INFO,
                "{0} users found",
                users.size());
        return users;
    }
    
    /**
     * Finds all technicians
     * @return a list with all technicians
     * @throws Exception 
     */
    @Override
    public List<Technician> findAllTechnicians() throws Exception {
        LOGGER.info("Fetching all technicians");
        List<Technician> technicians = null;
        tx = session.beginTransaction();
        technicians = session.createNamedQuery("findAllTechnicians")
                .getResultList();
        
        if (technicians == null || technicians.isEmpty()){
            tx.rollback();
            throw new NoTechnicianException("No se encontraron técnicos");
        }
           
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                technicians.size());
        tx.commit();
        return technicians;
    }

//    /**
//     * Assigns the ticket to technician corresponding to ticket technician
//     * @param ticket the ticket to assign
//     * @throws java.lang.Exception
//     */
//    @Override
//    public void assignTicket(Ticket ticket) throws Exception {
//        LOGGER.info("Assigning ticket");
//        tx = session.beginTransaction();
//        session.merge(ticket);
//        User technician = ticket.getTechnician();
//        session.merge(technician);
//        tx.commit();
//        LOGGER.info("Ticket assigned");
//    }

//    /**
//     * Updates the ticket data with the given ticket information
//     * @param ticket ticket whose state is going to change
//     * @throws Exception
//     */
//    @Override
//    public void changeState(Ticket ticket)throws Exception {
//        LOGGER.info("Changing ticket state");
//        tx = session.beginTransaction();
//        session.merge(ticket);
//        tx.commit();
//        LOGGER.info("Ticket state changed");
//    }

    /**
     * Checks if the credentials of a user exist with login and password
     * @param login String that contains the email of the user
     * @param password String that contains the password of the user
     * @return the user with that login and password
     * @throws Exception 
     */
    @Override
    public User authenticate(String login, String password) throws Exception {
        LOGGER.info("Authenticating user");
        tx = session.beginTransaction();
        List<User> users = null;
        String passwordHash = PasswordHandler.getHash(password, login);
        users = session.createNamedQuery("findUserByLogin")
                .setParameter("login", login)
                .setParameter("password", passwordHash)
                .getResultList();
        if (users == null || users.isEmpty()){
            tx.rollback();
            throw new UserLoginException("Usuario o contraseña invalidos");
        }
            
        User user = users.get(0);
        user.getCredential().setLastAccess(new Date());
        session.merge(user);
        session.flush();
        session.refresh(user);
        tx.commit();
        LOGGER.info("Log in successful");
        return user;
    }

    /**
     * Find all departments
     * @return a list with a list with all the departments - if no tickets
     * match raises a NoDepartmentException
     * @throws Exception 
     */
    @Override
    public List<Department> findAllDepartments() throws Exception {
        LOGGER.info("Fetching department by name");
        List<Department> departments = null;
        tx = session.beginTransaction();
        departments = session.createNamedQuery("findAllDepartments")
                .getResultList();
        
        if (departments == null || departments.isEmpty()) {
            tx.rollback();
            throw new NoDepartmentException("No se encontraron departamentos.");
        }
        LOGGER.log(Level.INFO,
                "{0} deparments found",
                departments.size());
        tx.commit();
        return departments;
    }

    /**
     * Finds all tickets that have the given state
     * @param state STATE to filter the ticket(OPEN,INPROGRESS,BLOQUED,CLOSED)
     * @return a list with the tickets that match with the state - if no tickets
     * match raises a NoTicketException
     * @throws Exception 
     */
    @Override
    public List<Ticket> findTicketsByState(STATE state) throws Exception {
        LOGGER.info("Fetching tickets by state");
        List<Ticket> tickets = null;
        tx = session.beginTransaction();
        tickets = session.createNamedQuery("findTicketsByState")
                .setParameter("state", state)
                .getResultList();
        
        if (tickets == null || tickets.isEmpty()) {
            tx.rollback();
            throw new NoTicketException("No se encontraron tickets.");
        }
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                tickets.size());
        tx.commit();
        return tickets;
    }

    /**
     * Finds all the tickets that have given technician assigned
     * @param login String login email of the user
     * @return  a list with the tickets that have assigned that technician
     * - if no tickets match raises a NoTicketException
     * @throws Exception 
     */
    @Override
    public List<Ticket> findTicketsByTechnician(String login) throws Exception {
        LOGGER.info("Fetching tickets by technician");
        List<Ticket> tickets = null;
        tx = session.beginTransaction();
        tickets = session.createNamedQuery("findTicketsByTechnician")
                .setParameter("login", login)
                .getResultList();
        if (tickets == null || tickets.isEmpty()) {
            tx.rollback();
            throw new NoTicketException("El técnico no tiene tickets asignados");
        }
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                tickets.size());
        tx.commit();
        return tickets;
    }

    /**
     * Finds all the users who have that login
     * @param login user email
     * @return a list of user who match with the login
     * @throws Exception 
     */
    @Override
    public List<User> findUserByLogin(String login) throws Exception {
        LOGGER.info("Fetching users by login");
        List<User> users = null;
        tx = session.beginTransaction();
        users = session.createNamedQuery("findUserById")
                .setParameter("login", login)
                .getResultList();
        LOGGER.log(Level.INFO,
                "{0} tickets found",
                users.size());
        tx.commit();
        return users;
    }

    /**
     * Updates a ticket and if ticket has technician then set up the ticket to the
     * technician list of tickets
     * @param ticket ticket with the data to update
     * @throws Exception 
     */
    @Override
    public void updateTicket(Ticket ticket) throws Exception {
        LOGGER.info("Updating ticket");
        tx = session.beginTransaction();
        session.merge(ticket);
        session.flush();
        session.refresh(ticket);
        if (ticket.getTechnician() != null) {
            Technician technician = ticket.getTechnician();
            List<Ticket> tickets = technician.getAssignedTickets();
            tickets.add(ticket);
            session.merge(technician);
            session.flush();
            session.refresh(technician);
            sendNotification(ticket, TICKET_UPDATE_TECHNICIAN);
            sendNotification(ticket, USER_TICKET_UPDATE_TECHNICIAN);
        }
        tx.commit();
        sendNotification(ticket, TICKET_STATE_CHANGE);
        LOGGER.info("Ticket updated");
    }

    private void sendNotification(Ticket ticket, String tag) throws Exception {
        String address;
        String message;
        String subject;
        List<String> addresses = new ArrayList<>();
        switch (tag) {
            case TICKET_CREATED:
                List<Technician> technicians = findAllTechnicians();
                if (!technicians.isEmpty()) {
                    for (Technician technician : technicians)
                        addresses.add(technician.getLogin());
                    message = "A new Ticket was created:\n\n"
                        + "Subject: " + ticket.getTitle() + "\n"
                        + "User: " + ticket.getUser();
                    subject = "New Ticket";
                    EmailSender.sendEmail(addresses, subject, message);
                }
                break;
            case TICKET_STATE_CHANGE:
                message = "Your ticket's state has changed.\n\n"
                        + "State: " + ticket.getState().name();
                subject = "Ticket: #" + ticket.getId() + " state changed.";
                address = ticket.getUser().getLogin();
                addresses.add(address);
                EmailSender.sendEmail(addresses, subject, message);
                break;
            case TICKET_UPDATE_TECHNICIAN:
                User activeUser = (User) SESSION_CONTENT.get("activeId");
                address = ticket.getTechnician().getLogin();
                addresses.add(address);
                subject = "Ticket assigned";
                message = "You have a new ticket assignation.\n\n"
                        + "Ticket: #" + ticket.getId()
                        + "\nSubject: " + ticket.getTitle();
                EmailSender.sendEmail(addresses, subject, message);
                break;
            case USER_TICKET_UPDATE_TECHNICIAN:
                address = ticket.getUser().getLogin();
                addresses.add(address);
                subject = "Technician assigned to your ticket #" + ticket.getId();
                message = "A technician has been assigned to your ticket.\n\n"
                        + "Ticket: #" + ticket.getId()
                        + "\nSubject: " + ticket.getTitle();
                EmailSender.sendEmail(addresses, subject, message);
                break;
        }
    }
}
