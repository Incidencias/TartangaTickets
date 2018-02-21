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
 *
 * @author ubuntu
 */

@Transactional(rollbackOn = Exception.class)
public class Logic implements LogicInterface {
    
    private static final Logger LOGGER = Logger.getLogger("com.tartangatickets.logic");
    private final SessionFactory factory = HibernateUtil.getSessionFactory();
    private final Session session = factory.openSession();
    private Transaction tx = null;
    private static HashMap sessionContent = new HashMap<>();
    
    @Override
    public HashMap getSessionContent() {
        return sessionContent;
    }
    
    @Override
    public void createTicket(Ticket ticket) {
        LOGGER.info("Creating ticket");
        tx = session.beginTransaction();
        ticket.setCreateDate(new Date());
        session.persist(ticket);
        tx.commit();
        // TODO Send email
        LOGGER.info("Ticket created");
    }

    @Override
    public void sendMessage(Message message) {
        LOGGER.info("Creating ticket message");
        tx = session.beginTransaction();
        //session.persist(message);
        Ticket ticket = message.getTicket();
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        ticket.setMessages(messages);
        session.merge(ticket);
        // TODO send email
        LOGGER.info("Ticket message created");
    }

    @Override
    public List<Ticket> findTicketsByUser(String userLogin) 
            throws NoTicketException {
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
    }

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

    @Override
    public void deleteUser(User user) {
        LOGGER.info("Deleting user");
        tx = session.beginTransaction();
        session.remove(user);
        tx.commit();
        LOGGER.info("User deleted");
    }

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

    @Override
    public void assignTicket(Ticket ticket) {
        LOGGER.info("Assigning ticket");
        tx = session.beginTransaction();
        session.merge(ticket);
        User technician = ticket.getTechnician();
        session.merge(technician);
        tx.commit();
        LOGGER.info("Ticket assigned");
    }

    @Override
    public void changeState(Ticket ticket) {
        LOGGER.info("Changing ticket state");
        tx = session.beginTransaction();
        session.merge(ticket);
        tx.commit();
        // TODO send message to user
        LOGGER.info("Ticket state changed");
    }

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
        return tickets;
    }

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
        return users;
    }
}
