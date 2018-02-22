///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.tartangatickets.views;
//
//import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
//import com.gluonhq.charm.glisten.application.MobileApplication;
//import com.gluonhq.charm.glisten.control.AppBar;
//import com.gluonhq.charm.glisten.control.CardPane;
//import com.gluonhq.charm.glisten.mvc.View;
//import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
//import com.tartangatickets.TartangaTickets;
//import com.tartangatickets.entities.Message;
//import com.tartangatickets.entities.Ticket;
//import com.tartangatickets.entities.User;
//import com.tartangatickets.logic.LogicInterface;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//
///**
// * Handle the Message window 
// *  
// *  <ul>
// *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
// *      <li><strong>sessionContent:</strong> HasMap from logic</li>
//
// *  </ul>
// *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
// *  @version 1.0, Feb 21 2018
// */
//public class MessageController {
//    
//    private static final Logger logger= Logger.getLogger("views message controller.");
//
//    @FXML
//    private View mensajes;
//    @FXML
//    private CardPane<Label> cPMessage = new CardPane();
//    @FXML
//    private Label lblNewMessage;
//    @FXML
//    private TextArea tANewMessage;
//    @FXML
//    private Button btnSend;
//    
//    private LogicInterface logic = TartangaTickets.LOGIC; 
//    private HashMap sessionContent = logic.getSessionContent();
//    
//    private List<Ticket> tickets =  new ArrayList<Ticket>();
//    private User user;
//    private Ticket ticket;
//    private Message message;
//    private Integer ticketId;
//    private int i=0;
//    
//    /**
//     * First actions when initialize the window
//     *  -Set up AppBar
//     *  -Gets the logged user and selected ticket id from sessionContent HasMap 
//     *  -Fill cardpane with messages
//     */
//    public void initialize(/*URL url, ResourceBundle rb*/) {
//        mensajes.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
//        mensajes.showingProperty().addListener((obs, oldValue, newValue) -> {
//            if (newValue) {
//                AppBar appBar = MobileApplication.getInstance().getAppBar();
//                Button back = MaterialDesignIcon.ARROW_BACK.button();
//                back.setOnAction(event -> 
//                    MobileApplication.getInstance().switchToPreviousView()
//                );
//                appBar.setNavIcon(back);
//                logger.info("inizialize message controller.");
//                user = (User) sessionContent.get("activeId");
//                ticketId = (Integer) sessionContent.get("ticketId");
//                
//                findTicket();
//                
//                for(i=0; i<ticket.getMessages().size(); i++){
//                    String m = ticket.getMessages().get(i).getUser().getFullName()+": "+ticket.getMessages().get(i).getBody();
//                    cPMessage.getItems().add(label(m));
//                }
//            }
//        });
//    }  
//    /**
//     * Set up a new label with padding with a given string
//     * @param caption String to set up the label
//     * @return the created label
//     */
//    private Label label( String caption ) {
//        Label label = new Label( caption );
//        label.setStyle("-fx-padding:10;");
//        return label;
//    }
//    
//    /**
//     * Send a message with the window fields data
//     * @throws IOException 
//     */
//    @FXML
//    private void handleButtonSend() throws IOException{
//        logger.info("Sending message.");
//        try {
//            message = new Message();
//            message.setBody(tANewMessage.getText());
//            message.setTicket(ticket);
//            message.setUser(user);
//            logic.sendMessage(message);
//            String m = user.getFullName()+": "+message.getBody().toString();
//            cPMessage.getItems().add(label(m));
//            tANewMessage.setText("");
//        } catch (Exception ex) {
//            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    /**
//     * Get the ticket from the id of selected ticket stored in sessionContent HasMap
//     */
//    private void findTicket() {
//        try {
//            tickets = logic.findAllTickets();
//            for(int i=0; i<tickets.size(); i++){
//                if(tickets.get(i).getId().equals(ticketId)){
//                    ticket = tickets.get(i);
//                }
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//                
//    }
//    
//}
