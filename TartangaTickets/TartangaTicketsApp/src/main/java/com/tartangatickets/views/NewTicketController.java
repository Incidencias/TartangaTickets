package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.User;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.Message;
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class NewTicketController {
    private static final Logger logger = Logger.getLogger(NewTicketController.class.getName());

    @FXML
    private View nueva_incidencia;
    @FXML
    private Label lblTicket;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblDepartment;
    @FXML
    private TextField tfLocation;
    @FXML
    private TextField tfMachineCode;
    @FXML
    private Label lblCreateDate;
    @FXML
    private TextField tfMessage;
    @FXML
    private Button btnCreateTicket;
    private final LogicInterface logic = TartangaTickets.LOGIC;
    private final HashMap sessionContent = logic.getSessionContent();
    private final User user = (User) sessionContent.get("activeId");
    private List<User> users;
    private Ticket ticket;
    private Message message;
    
    /**
     * Initializes the controller class.
     */
    public void initialize() {
        nueva_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO arrowback button function
            }
        });
        
        try{
            users = logic.findAllUsers();
        }catch(Exception ex){
            logger.info("Aplication error.");
        }
        
        
    }     
    
    private void handleButtonCreateTicket() throws Exception {
        logger.info("Create Ticket Action event.");
        if(tfMachineCode.getText().trim().isEmpty() && tfLocation.getText().trim().isEmpty() && tfMessage.getText().trim().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR,"Los datos no pueden estar vacíos.",ButtonType.OK);
            alert.showAndWait();
        }else{
            lblTicket.setText(ticket.getId().toString());
            lblUser.setText(user.getName());
            lblDepartment.setText(user.getDepartment().getName());
            ticket.setLocation(tfLocation.getText());
            ticket.setMachineCode(tfMachineCode.getText());
            lblCreateDate.setText(ticket.getCreateDate().toString());
            message.setBody(tfMessage.getText());
            message.setTicket(ticket);
            message.setUser(user);
            ticket.getMessages().add(message);
        }
        try {  
            logic.createTicket(ticket);
            MobileApplication.getInstance().switchView("TicketView");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Datos erroneos");
            alert.showAndWait();
        }
    } 
}
