/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.logic;


import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.utils.EmailSender;
import com.tartangatickets.utils.HibernateUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author ubuntu
 */
public class Logic implements LogicInterface {
    
    private static final Logger LOGGER = Logger.getLogger("com.tartangatickets.logic");
    private final SessionFactory factory = HibernateUtil.getSessionFactory();
    protected Session session = factory.openSession();
    Transaction tx = null;
    
    
    @Override
    public void createTicket(Ticket ticket) throws Exception {
        LOGGER.info("Creating ticket");
        try {
            tx = session.beginTransaction();
            ticket.setCreateDate(new Date());
            session.persist(ticket);
            tx.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Exception creating ticket. {0}",
                    e.getMessage());
            tx.rollback();
            throw new Exception();
        }
        LOGGER.info("Ticket created");
    }

    @Override
    public void sendMessage(Message message) throws Exception {
        LOGGER.info("Creating ticket message");
        try {
            tx = session.beginTransaction();
            session.persist(message);
            Ticket ticket = message.getTicket();
            session.merge(ticket);
            tx.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Exception creating ticket message. {0}",
                    e.getMessage());
            tx.rollback();
            throw new Exception();
        }
        LOGGER.info("Ticket message created");
    }

    @Override
    public List<Ticket> findTicketsByUser(Integer userId) throws Exception {
        List<Ticket> tickets = null;
        try {
            tx = session.beginTransaction();
            tickets = session.createNamedQuery("findTicketsByUser")
                    .setParameter("user.id", userId)
                    .getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
        return tickets;
    }

    @Override
    public List<Ticket> findAllTickets() throws Exception {
        List<Ticket> tickets = null;
        try {
            tx = session.beginTransaction();
            tickets = session.createNamedQuery("findAllTickets")
                    .getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
        return tickets;
    }

    @Override
    public void changePassword(Credential credential, Byte[] newPassword) {
        // TODO
    }

    @Override
    public void recoverPassword(Integer userId) throws Exception {
        try {
            User user = (User) session.createNamedQuery("findUserById")
                .setParameter("id", userId)
                .getSingleResult();
            EmailSender.sendSimpleEmail(user.getEmail());
        } catch (Exception e) {
            throw new Exception();
        }
        
    }

    @Override
    public void createUser(User user) throws Exception {
        try {
            tx = session.beginTransaction();
            session.persist(user);
            Credential credential = user.getCredential();
            session.persist(credential);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
    }

    @Override
    public void deleteUser(User user) throws Exception {
        try {
            tx = session.beginTransaction();
            session.remove(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
    }

    @Override
    public List<User> findAllUsers() throws Exception {
        List<User> users = null;
        try {
            tx = session.beginTransaction();
            users = session.createNamedQuery("findAllUsers")
                    .getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
        return users;
    }

    @Override
    public void assignTicket(Ticket ticket) throws Exception {
        try {
            tx = session.beginTransaction();
            session.merge(ticket);
            User technician = ticket.getTechnician();
            session.merge(technician);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
    }

    @Override
    public void changeState(Ticket ticket) throws Exception {
        try {
            tx = session.beginTransaction();
            session.merge(ticket);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
    }

    @Override
    public User authenticate(String login, Byte[] password) throws Exception {
        User user = null;
        try {
            user = (User) session.createNamedQuery("findUserByLogin")
                    .setParameter("credential.login", login)
                    .setParameter("credential.password", password)
                    .getSingleResult();
        } catch (Exception e) {
            throw new Exception();
        }
        return user;
    }   

    @Override
    public void createTechnician(Technician technician) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteTechnician(Technician technician) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateTechnician(Technician technician) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Department findDepartmentByName(String departmentName) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
