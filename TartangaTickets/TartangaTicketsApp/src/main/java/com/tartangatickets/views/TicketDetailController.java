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
import com.tartangatickets.exceptions.NoTicketException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Iker Jon Mediavilla
 */
public class TicketDetailController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    
    @FXML
    private View detalles_incidencia;
    @FXML
    private Label lblTitleTicket;
    @FXML
    private Label lblUserTicket;
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
    private Button btnStore;
    @FXML
    private ComboBox<Technician> comboTechnician;
    
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSessionContent();
    private User user;
    
    private Ticket ticket;
    private int i = 0; 
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
                back.setOnAction(event -> {
                    sessionContent.remove("activeId");
                    MobileApplication.getInstance().switchToPreviousView();
                });
                appBar.setNavIcon(back);
                user = (User) sessionContent.get("activeId");
                Integer ticketId = (Integer) sessionContent.get("ticketId");
                getTicket(ticketId);
                
                if(!(user instanceof Technician)){
                    btnEditState.setVisible(false);
                    btnStore.setVisible(false);
                    comboTechnician.setEditable(true);
                }
                fillTechniciansCombo();
                loadData();
            }
        });
    }
    
    @FXML
    private void handleButtonSendMessage() throws IOException{
        MobileApplication.getInstance().switchView("MessageView");
    }
    
    @FXML
    private void handleButtonEditState() throws IOException{
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label("Seleccione estado"));
        dialog.setContent(new Label("Asignar un estado a la incidencia: "));
        Button btnOpen = new Button("Abierto");
        Button btnInProgress = new Button("Progreso");
        Button btnBloqued = new Button("Bloqueado");
        Button btnClosed = new Button("Cerrado");
        btnOpen.setOnAction(e -> {
            if(ticket.getState().equals(STATE.OPEN)){
                dialog.hide();
            }
            else{
                ticket.setState(STATE.OPEN);
                dialog.hide();
            }
        });
        btnInProgress.setOnAction(e -> {
            if(ticket.getState().equals(STATE.INPROGRESS)){
                dialog.hide();
            }
            else{
                ticket.setState(STATE.INPROGRESS);
                dialog.hide();
            }
        });
        btnBloqued.setOnAction(e -> {
            if(ticket.getState().equals(STATE.BLOQUED)){
                dialog.hide();
            }
            else{
                ticket.setState(STATE.BLOQUED);
                dialog.hide();
            }
        });
        btnClosed.setOnAction(e -> {
            if(ticket.getState().equals(STATE.CLOSED)){
                dialog.hide();
            }
            else{   
                ticket.setState(STATE.CLOSED);
                dialog.hide();
            }
        });
        dialog.getButtons().addAll(btnOpen, btnInProgress, btnBloqued, btnClosed);
        dialog.showAndWait();
        updateState();
    }

    
    public void handleComboBoxTechnician(){
        Technician technician = 
                comboTechnician.getSelectionModel().getSelectedItem();
        if (technician.getLogin() != null) {
            ticket.setTechnician(technician); 
        } else {
            ticket.setTechnician(null);
        }
    }
    
    public void handleButtonStore() {
        try {
            logic.updateTicket(ticket);
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
    }
    
    private List<Technician> getAllTechnicians() {
        List<Technician> technicians = null;
        try {
            technicians = logic.findAllTechnicians();
        } catch (NoTechnicianException ex) {
            DialogHelper.newInstance("INFO", ex.getMessage());
            comboTechnician.setDisable(true);
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return technicians;
    }
    
    private void fillTechniciansCombo() {
        itemsTechnicians = FXCollections.observableArrayList();
        List<Technician> technicians = getAllTechnicians();
        if (technicians == null || technicians.isEmpty())
            return;
        technicians.forEach((technician) -> {
            itemsTechnicians.add(technician);
        });
        itemsTechnicians.add(0, getEmptyTechnician());
        
        comboTechnician.getItems().addAll(itemsTechnicians);
    }

    private Technician getEmptyTechnician() {
        Technician technician = new Technician();
        technician.setLastName1("");
        technician.setName("");
        return technician;
    }
   
    // Checked
    private void loadData() {
        lblTitleTicket.setText(ticket.getId().toString());
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
        lblStateTicket.setText(ticket.getState().name().toLowerCase());
        if(ticket.getCreateDate()!=null){
            lblCreateDateTicket.setText(formato.format(ticket.getCreateDate()));                                     
        }
        if(ticket.getEndDate()!=null){
                    
            lblEndDateTicket.setText(formato.format(ticket.getEndDate()));                
        }
    }

    private void getTicket(Integer ticketId) {
        try {
            ticket = logic.findTicketById(ticketId);
        } catch (NoTicketException ex) {
            MobileApplication.getInstance().switchView("TicketListView");
            DialogHelper.newInstance("INFOR", ex.getMessage());
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
    }

    private void updateState() {
        lblStateTicket.setText(ticket.getState().name().toLowerCase());
    }
}
