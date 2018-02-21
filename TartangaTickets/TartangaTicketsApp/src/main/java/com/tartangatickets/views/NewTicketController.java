package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
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
import com.tartangatickets.utils.DialogHelper;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private TextField tfAsunto;
    @FXML
    private Button btnCreateTicket;
    private final LogicInterface logic = TartangaTickets.LOGIC;
    private final HashMap sessionContent = logic.getSessionContent();
    private final User user = (User) sessionContent.get("activeId");
    
    /**
     * Initializes the controller class.
     */
    public void initialize() {
        nueva_incidencia.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        nueva_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
            }
        });
        lblUser.setText(user.getFullName());
        lblDepartment.setText(user.getDepartment().getName());
        lblCreateDate.setText(new Date().toString());
    }     
    
    @FXML
    private void handleButtonCreateTicket() {
        Ticket ticket = null;
        if(tfMachineCode.getText().trim().isEmpty() && tfLocation.getText().trim().isEmpty()&& tfAsunto.getText().trim().isEmpty()){
            DialogHelper.newInstance("ERROR",
                    "Los datos no pueden estar vacíos.");

        }else{
            ticket = new Ticket();
            ticket.setLocation(tfLocation.getText());
            ticket.setMachineCode(tfMachineCode.getText());
            ticket.setState(State.OPEN);
            ticket.setUser(user);
            ticket.setDepartment(user.getDepartment());
            ticket.setTitle(tfAsunto.getText());

        }
        try {  
            logic.createTicket(ticket);
            DialogHelper.newInstance("INFO", INFO_TICKET_CREADO);
            MobileApplication.getInstance().switchView("TicketView");
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR",
                    "Datos erroneos.");
        }
    } 
}
