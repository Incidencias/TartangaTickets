/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.logic;

import com.tartangatickets.utils.HibernateUtil;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
        try {
            tx = session.beginTransaction();
            session.persist(ticket);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
        
    }

    @Override
    public void sendMessage(Message message) throws Exception {
        try {
            tx = session.beginTransaction();
            session.persist(message);
            Ticket ticket = message.getTicket;
            session.merge(ticket);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
    }

    @Override
    public List<Ticket> findTicketsByUser(Integer userId) throws Exception {
        List<Ticket> tickets = null;
        try {
            tx = session.beginTransaction();
            tickets = session.createNamedQuery("findTicketsByUser")
                    .setParameter("user_id", userId)
                    .getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new Exception();
        }
        return tickets;
    }

    @Override
    public Object findAllTickets() throws Exception {
        List<Ticket> tickets = null
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
    public void changePassword(Credential credential, String newPassword) {
        // TODO
    }

    @Override
    public void recoverPassword(Integer userId) throws Exception {
        //TODO
    }

    @Override
    public void createUser(User user) throws Exception {
        try {
            tx = session.beginTransaction();
            session.persist(ticket);
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
    public Object findAllUsers() throws Exception {
        List<User> users = null
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
    public void authenticate(String login, String password) throws Exception {
        // TODO
    }
    
    
}
