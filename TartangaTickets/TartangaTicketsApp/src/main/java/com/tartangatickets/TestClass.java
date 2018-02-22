/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets;

import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ubuntu
 */
public class TestClass {
    
   private static LogicInterface logic = new Logic();
    
    public static void main(String [] args) {
        
        /*
        try {
            Encrypter.encryptFile();
        } catch (EncrypterException ex) {
        Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        createUser("juan@gmail.com","Juan","Guerra","Ortiz","SD","Sonido");
        createUser("Alex@gmail.com","Alex","Ríos","Ferreyra","INF","Informática");
        createUser("lobo@gmail.com","Manuel","Castro","Pereyra","IS","Integración social");
        createUser("nano@gmail.com","Luis","Peralta","Godoy","A3D","Animación 3D");
        createUser("test@gmail.com","Test","Quiroga","Castillo","A3D","Animación 3D");
        createUser("test2@gmail.com","Test2","Vega","Peralta","SD","Sonido");
        createUser("test3@gmail.com","Miguel","Vega","Carrizo","SD","Sonido");
        createUser("test4@gmail.com","Martín","Acuña","Soto","INF","Informática");
        createUser("test5@gmail.com","Ana","Franco","Toledo","IS","Integración social");
        createUser("test6@gmail.com","Pedro","Campos","Toledo","IS","Integración social");
        createUser("test7@gmail.com","María","Chávez","Arce","AM","Administración");
        createUser("test8@gmail.com","Marco","Polo","","AM","Administración");
        createUser("test9@gmail.com","Africa","Martín","","AM","Administración");


    //   deleteUser();

        createTechnician();

     //     createAdmin();

     //   createTicket();

    //    sendMessage();
        
      /*  
        try {   
            List<Ticket> tickets = logic.findTicketsByUser("jzaballazarzosa@gmail.com");
            System.out.print(tickets.size());
        } catch (Exception ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        List<Ticket> tickets = null;
        try {
            tickets = logic.findAllTickets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Ticket ticket = null;
        
        if (tickets != null) 
            ticket = tickets.get(0);
        
        ticket.setState(STATE.CLOSED);
        
        try {
            logic.changeState(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        Technician technician = (Technician) createTechnician();
        
        ticket.setTechnician(technician);
        
        try {
            logic.assignTicket(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            logic.findTicketsByUser("catxser@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        User user = null;
        try {
            user = logic.authenticate("catxser@gmail.com", "A1a+bbbb");
            System.out.print(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            logic.changePassword(user.getCredential(), "A1a+bbbb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            logic.recoverPassword("catxser@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            List<User> users = logic.findAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            logic.findAllDepartments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        try {
            logic.findTicketsByState(STATE.OPEN);
            logic.findTicketsByState(STATE.CLOSED);
            logic.findTicketsByState(STATE.BLOQUED);
            logic.findTicketsByState(STATE.INPROGRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        try {
            logic.findTicketsByTechnician("ikerjon.m.a@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.exit(0);
*/
    }
   
    
    private static void createUser(String correo,String nombre,String ap1,String ap2, String dp,String dpname) {
        Credential credential = new Credential();
        credential.setLogin(correo);
        credential.setPassword("Jon1#Jon");
        //credential.setLogin("catxser@gmail.com");
        User user = new User(
                credential.getLogin(),
                credential,
                nombre,
                ap1,
                ap2,
                new Department(dp, dpname),
                null);
        try {
            logic.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private static void deleteUser() {
        Credential credential = new Credential();
        credential.setLogin("jzaballazarzosa@gmail.com");
        credential.setPassword("Jon1#Jon");
        User user = new User(
                credential.getLogin(),
                credential,
                "Jon",
                "Zaballa",
                "Zarzosa",
                new Department("INF", "Informática"),
                null);
        try {
            logic.deleteUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private static User createTechnician() {
        User userReturn = null;
        Credential credential = new Credential();
        credential.setLogin("jzaballazarzosa@gmail.com");
        credential.setPassword("Jon1#Jon");
        //credential.setLogin("pepito@gmail.com");
        User user = new Technician(
                credential.getLogin(),
                credential,
                "Jon",
                "Zaballa",
                "Zarzosa",
                new Department("EL", "Electricidad"),
                null,
                null,
                false);
        try {
            userReturn = logic.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userReturn;
    }

    private static void createAdmin() {
        Credential credential = new Credential();
        credential.setLogin("catxser@gmail.com");
        User user = new Technician(
                credential.getLogin(),
                credential,
                "Sergio",
                "López",
                "Fuentefría",
                new Department("INF", "Informática"),
                null,
                null,
                true);
        try {
            logic.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTicket() {
        Credential credential = new Credential();
        credential.setLogin("jzaballazarzosa@gmail.com");
        credential.setPassword("Jon1#Jon");
        credential.setLogin("catxser@gmail.com");
        User user = new User(
                credential.getLogin(),
                credential,
                "Sergio",
                "López",
                "Fuentefría",
                new Department("INF", "Informática"),
                null);
        Ticket ticket = new Ticket("Ordenador averiado",new Date(), null, "001", new Department("INF", "Informática"), "Aula 1", STATE.OPEN, user, null, null);
        try {
            logic.createTicket(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    private static void sendMessage() {
        Credential credential = new Credential();
        credential.setLogin("jzaballazarzosa@gmail.com");
        credential.setPassword("Jon1#Jon");
        credential.setLogin("catxser@gmail.com");
        User user = new User(
                credential.getLogin(),
                credential,
                "Sergio",
                "López",
                "Fuentefría",
                new Department("INF", "Informática"),
                null);
        Ticket ticket = new Ticket("Ordedor averiado",null, null, "001", new Department("INF", "Informática"), "Aula 1", STATE.OPEN, user, null, null);
        ticket.setId(new Integer(8));
        Message message = new Message("Todavaía no está arreglado, putes", ticket, user);
        try {
            logic.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changePassword() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */
}
