package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Iker Jon Mediavilla
 */
public class TicketDetailController {
    
    private static final Logger logger= Logger.getLogger("view Ticket Detail");
   
    private Stage stage;
    
    @FXML
    private View detalles_incidencia;
    
    @FXML
    private Label lblIdTicket;
    @FXML
    private Label lblUserTicket;
    @FXML
    private Label lblTechnicianTicket;
    @FXML
    private Label lblDepartmentTicket;
    @FXML
    private Label lblLocationTicket;
    @FXML
    private Label lblMachineCodeTicket;
    @FXML
    private Label lblStateTicket;
    @FXML
    private Label lblCreateDateTicket;
    @FXML
    private Label lblEndDateTicket;
    @FXML
    private Button btnSendMessage;
    
    private User user;
    
    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void initialize() {
        detalles_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                stage.setOnShowing(this::handleWindowShowing);
            }
        });
        
        
    }
    
    private void handleWindowShowing(){
        int i=0;
        Ticket ticket = user.getCreatedTickets().get(i);
        lblIdTicket.setText(ticket.getId().toString());
        lblUserTicket.setText(ticket.getUser().toString());
        lblTechnicianTicket.setText(ticket.getTechnician().toString());
        lblDepartmentTicket.setText(ticket.getDepartment());
        lblLocationTicket.setText(ticket.getLocation());
        lblMachineCodeTicket.setText(ticket.getMachineCode());
        lblStateTicket.setText(ticket.getState().toString());
        lblCreateDateTicket.setText(ticket.getCreateDate().toString());
        lblEndDateTicket.setText(ticket.getEndDate().toString());
    }
    
    private void handleButtonSendMessage() throws IOException{
        logger.info("Going to send Message Action event");
        MobileApplication.getInstance().switchView("MessageView");
    }
    
}
