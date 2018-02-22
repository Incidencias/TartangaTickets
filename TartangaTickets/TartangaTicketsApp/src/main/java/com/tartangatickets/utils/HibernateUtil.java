/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Configuration of hibernate, set up classes
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    /**
     * Set up the classes in hibernate and get data from hibernate configuration file
     * @return SessionFactory - data from hibernate configuration file
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration config = new Configuration();
            config.addAnnotatedClass(Credential.class);
            config.addAnnotatedClass(Department.class);
            config.addAnnotatedClass(Message.class);
            config.addAnnotatedClass(Technician.class);
            config.addAnnotatedClass(Ticket.class);
            config.addAnnotatedClass(User.class);

            // load hibernate.cfg.xml from different directory
            SessionFactory sessionFactory = config.configure(
                    "com/tartangatickets/hibernateconf/hibernate.cfg.xml")
                    .buildSessionFactory();
            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * close the sessionFactory
     */
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
