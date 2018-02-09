/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CardPane;
import com.gluonhq.charm.glisten.mvc.View;
import com.sun.mail.handlers.message_rfc822;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.DefaultProperty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Control;

import javafx.scene.control.TextArea;
import static javafx.scene.input.KeyCode.T;
import static sun.security.jgss.GSSUtil.login;

/**
 * FXML Controller class
 *
 * @author Iker Jon
 */
public class MessageController {
    
    private static final Logger logger= Logger.getLogger("views message controller.");

    @FXML
    private View mensajes;
    @FXML
    private CardPane<Label> cPMessage;
    @FXML
    private Label lblNewMessage;
    @FXML
    private TextArea tANewMessage;
    @FXML
    private Button btnSend;
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    
    private User user;
    private Ticket ticket;
    private Message message;
    private int i=0;
    
    /**
     * Initializes the controller class.
     */
    
    public void initialize(URL url, ResourceBundle rb) {
        mensajes.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                logger.info("inizialize message controller.");
                user = (User) sessionContent.get("activeId");
                ticket = (Ticket) sessionContent.get("ticketId");
                cPMessage = new CardPane();
                for(i=0; i<ticket.getMessages().size(); i++){
                    String m = ticket.getMessages().get(i).getUser().getName()+": "+ticket.getMessages().get(i).getBody();
                    cPMessage.getItems().add(label(m));
                }
            }
        });
    }  
    
    private Label label( String caption ) {
        Label label = new Label( caption );
        label.setStyle("-fx-padding:10;");
        return label;
    }
    
    private void handleButtonSend() throws IOException{
        logger.info("Sending message.");
        try {
            message = new Message();
            message.setBody(tANewMessage.toString());
            message.setTicket(ticket);
            message.setUser(user);
            logic.sendMessage(message);
            String m = user.getName()+": "+message.getBody();
            cPMessage.getItems().add(label(m));
        } catch (Exception ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
