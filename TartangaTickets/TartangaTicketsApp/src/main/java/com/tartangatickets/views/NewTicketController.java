package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.logic.LogicInterface;
import java.util.Date;
import com.tartangatickets.utils.DialogHelper;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Handle the new ticket window 
 *  
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *      <li><strong>sessionContent:</strong> HasMap from logic</li>
 *      <li><strong>user:</strong> logged user from sessionContent</li>

 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class NewTicketController {

    private static final String GENERAL_ERROR = "Error inesperado.";
    private static final String INFO_TICKET_CREADO = "Incidencia registrada.";
    
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
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    private final User user = (User) sessionContent.get("activeId");
    
    /**
     * First actions when initialize the window
     * -Set up the AppBar
     * -Fill some fields with logged user data
     */
    public void initialize() {
        nueva_incidencia.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        nueva_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Crear Incidencia");
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> {
                    MobileApplication.getInstance().switchView(TartangaTickets.MAINMENU_VIEW);
                    MobileApplication.getInstance().removeViewFactory(TartangaTickets.NEWTICKET_VIEW);
                });
                appBar.setNavIcon(back);
            }
        });
        lblUser.setText(user.getFullName());
        lblDepartment.setText(user.getDepartment().getName());
        lblCreateDate.setText(new Date().toString());
    }     
    
    /**
     * Creates a new ticket with the data of fields in the view 
     */
    @FXML
    private void handleButtonCreateTicket() {
        Ticket ticket = null;
        if(tfMachineCode.getText().trim().isEmpty() || tfLocation.getText().trim().isEmpty() || tfAsunto.getText().trim().isEmpty()){
            DialogHelper.newInstance(
                    "ERROR",
                    "Los campos no pueden estar vacíos."
            );
        }else{
            ticket = new Ticket();
            ticket.setLocation(tfLocation.getText());
            ticket.setMachineCode(tfMachineCode.getText());
            ticket.setState(STATE.OPEN);
            ticket.setUser(user);
            ticket.setDepartment(user.getDepartment());
            ticket.setTitle(tfAsunto.getText());
            try {  
                logic.createTicket(ticket);
                MobileApplication.getInstance().showMessage(INFO_TICKET_CREADO);
                MobileApplication.getInstance().switchView(TartangaTickets.TICKET_LIST_VIEW);
                MobileApplication.getInstance().removeViewFactory(TartangaTickets.NEWTICKET_VIEW);
            } catch (Exception ex) {
                DialogHelper.newInstance("ERROR",GENERAL_ERROR);
            }
        }
    } 
}
