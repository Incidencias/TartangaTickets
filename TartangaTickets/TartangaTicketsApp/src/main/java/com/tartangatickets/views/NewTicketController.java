package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.Message;
import com.tartangatickets.entities.State;
import com.tartangatickets.logic.LogicInterface;
import java.util.Date;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class NewTicketController {

    private static final String GENERAL_ERROR = "Error inesperado.";
    private static final String INFO_TICKET_CREADO = "Ticket creado.";
    
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
        lblUser.setText(user.getFullName());
        lblDepartment.setText(user.getDepartment().getName());
        lblCreateDate.setText(new Date().toString());
    }     
    
    @FXML
    private void handleButtonCreateTicket() {
        Ticket ticket = null;
        if(tfMachineCode.getText().trim().isEmpty() && tfLocation.getText().trim().isEmpty()){
            Alert alert=new Alert(
                    Alert.AlertType.ERROR,
                    "Los datos no pueden estar vacíos.",
                    ButtonType.OK
            );
            alert.showAndWait();
        }else{
            ticket = new Ticket();
            ticket.setLocation(tfLocation.getText());
            ticket.setMachineCode(tfMachineCode.getText());
            ticket.setState(State.OPEN);
            ticket.setUser(user);
            ticket.setDepartment(user.getDepartment());
            if (tfMessage.getText().trim().isEmpty()) {
                Message message = new Message();
                message.setBody(tfMessage.getText());
                message.setTicket(ticket);
                message.setUser(user);
                ticket.getMessages().add(message);
            }
        }
        try {  
            logic.createTicket(ticket);
            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION, 
                    INFO_TICKET_CREADO,
                    ButtonType.OK
            );
            alert.showAndWait();
            MobileApplication.getInstance().switchView("TicketView");
        } catch (Exception ex) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    GENERAL_ERROR,
                    ButtonType.OK
            );
            alert.showAndWait();
        }
    } 
}