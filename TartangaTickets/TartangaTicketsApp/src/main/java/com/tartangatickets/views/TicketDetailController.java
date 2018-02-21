package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoTechnicianException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Iker Jon Mediavilla
 */
public class TicketDetailController {
    
    private static final Logger logger= Logger.getLogger("view Ticket Detail");
   
    private Stage stage;
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    
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
    @FXML
    private Button btnEditState;
    @FXML
    private Button btnEditTechnician;
    @FXML
    private ComboBox<Technician> comboTechnician;
    
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    private User user;
    
    private Ticket ticket;
    private Date endDate;
    private int i = 0; 
    private Integer ticketId;
    private List<Ticket> tickets =  new ArrayList<Ticket>();
    private List<Technician> allTech = new ArrayList<Technician>();
    private ObservableList<Technician> itemsTechnicians;
    private int positionTech;

    DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    
    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void initialize() {
        detalles_incidencia.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        detalles_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                user = (User) sessionContent.get("activeId");
                ticketId = (Integer) sessionContent.get("ticketId");
                try {
                    tickets = logic.findAllTickets();
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                findTicket();
                
                if(user instanceof Technician){
                    btnEditState.setVisible(true);
                    btnEditTechnician.setVisible(true);
                    comboTechnician.setEditable(false);
                }
                else{
                    btnEditState.setVisible(false);
                    btnEditTechnician.setVisible(false);
                    comboTechnician.setEditable(true);
                }
                
                techniciansCombo();
                loadData();
                
                
            }
        });
        
        
    }
    
    @FXML
    private void handleButtonSendMessage() throws IOException{
        logger.info("Going to send Message Action event");
        MobileApplication.getInstance().switchView("MessageView");
    }
    @FXML
    private void handleButtonEditState() throws IOException{
        logger.info("Openning a dialog to change state. ");
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label("Editando el estdo"));
        dialog.setContent(new Label("Asignar un estado a la incidencia: "));
        Button btnOpen = new Button("Abierto");
        Button btnInProgress = new Button("En progreso");
        Button btnBloqued = new Button("Bloqueado");
        Button btnClosed = new Button("Cerrado");
        btnOpen.setOnAction(e -> {
            logger.info("Dialog Edit state Open");
            if(ticket.getState().equals(STATE.OPEN)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    ticket.setState(STATE.OPEN);
                    endDate = null;
                    ticket.setEndDate(endDate);
                    logic.changeState(ticket);
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
            
        });
        btnInProgress.setOnAction(e -> {
            logger.info("Dialog Edit state In progress");
            if(ticket.getState().equals(STATE.INPROGRESS)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    ticket.setState(STATE.INPROGRESS);
                    endDate = null;
                    ticket.setEndDate(endDate);
                    logic.changeState(ticket);
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        btnBloqued.setOnAction(e -> {
            logger.info("Dialog Edit state Bloqued");
            if(ticket.getState().equals(STATE.BLOQUED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    ticket.setState(STATE.BLOQUED);
                    endDate = null;
                    ticket.setEndDate(endDate);
                    logic.changeState(ticket);
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        btnClosed.setOnAction(e -> {
            logger.info("Dialog Edit state Close");
            if(ticket.getState().equals(STATE.CLOSED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{   
                try {
                    endDate = new Date();
                    ticket.setState(STATE.CLOSED);
                    ticket.setEndDate(endDate);
                    logic.changeState(ticket);
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        dialog.getButtons().addAll(btnOpen, btnInProgress, btnBloqued, btnClosed);
        dialog.showAndWait();
        loadData();
    }
    @FXML
    private void handleButtonNoTechnician() {        
        ticket.setTechnician(null);
        comboTechnician.getSelectionModel().clearSelection(-1);
        loadData();    
    }
    
    public void handleComboBoxTechnician(){        
        ticket.setTechnician(comboTechnician.getSelectionModel().getSelectedItem()); 
        loadData();
    }

    private void findTicket() {
        try {
            tickets = logic.findAllTickets();
            for(int i=0; i<tickets.size(); i++){
                if(tickets.get(i).getId().equals(ticketId)){
                    ticket = tickets.get(i);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    private List<Technician> getAllTechnicians() {
        List<Technician> technicians = null;
        try {
            technicians = logic.findAllTechnicians();
        } catch (NoTechnicianException ex) {
            comboTechnician.setDisable(true);
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return technicians;
    }
    
    private void techniciansCombo() {
        itemsTechnicians = FXCollections.observableArrayList();
        List<Technician> technicians = getAllTechnicians();
        technicians.forEach((technician) -> {
            itemsTechnicians.add(technician);
        });
        
        comboTechnician.getItems().addAll(itemsTechnicians);
    }
   

    private void loadData() {
        lblIdTicket.setText(ticket.getId().toString());
                
        lblUserTicket.setText(ticket.getUser().getFullName());
                
        
        if(ticket.getTechnician()==null){
            comboTechnician.getSelectionModel().select(-1);
        }
        else{         
            comboTechnician.setValue(ticket.getTechnician());                               
        }
        lblDepartmentTicket.setText(ticket.getDepartment().getName());               
        lblLocationTicket.setText(ticket.getLocation());
        lblMachineCodeTicket.setText(ticket.getMachineCode());
        lblStateTicket.setText(ticket.getState().toString());
        if(ticket.getCreateDate()!=null){
            lblCreateDateTicket.setText(formato.format(ticket.getCreateDate()));                                     
        }
        else{
            lblCreateDateTicket.setText(null);
        }
        if(ticket.getEndDate()!=null){
                    
            lblEndDateTicket.setText(formato.format(ticket.getEndDate()));                
        }
        else{
            lblEndDateTicket.setText(null);
            
        }
    }
}
