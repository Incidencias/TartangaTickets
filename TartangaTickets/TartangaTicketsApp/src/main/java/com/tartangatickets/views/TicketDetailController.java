package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Handle the ticket details window 
 *  
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *      <li><strong>sessionContent:</strong> HasMap from logic</li> 
 *      <li><strong>formatter:</strong> Simple Date Formate with "yyyy-MM-dd" format</li> 
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
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
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    private User user;
    private Ticket ticket;
    private ObservableList<Technician> itemsTechnicians;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
       /**
     * First actions when initialize the window
     * -Set up the AppBar
     * -Get the logged user
     * -Get the selected ticket with sessionContent ticketId
     * -Load data
     * -Set up visible fields if logged user is a technician
     * -Fill technician combobox
     */
    @FXML
    public void initialize() {
        detalles_incidencia.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        detalles_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                ticket = null;
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Incidencia");
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> {
                    sessionContent.remove("ticketId");
                    MobileApplication.getInstance().switchView(TartangaTickets.TICKET_LIST_VIEW);
                });
                Button home = MaterialDesignIcon.HOME.button();
                home.setOnAction(event -> {
                    sessionContent.remove("ticketId");
                    MobileApplication.getInstance().switchView(TartangaTickets.MAINMENU_VIEW);
                });
                Button store = MaterialDesignIcon.SAVE.button();
                store.setOnAction(event -> {
                    updateTicket();
                    MobileApplication.getInstance().switchView(TartangaTickets.TICKET_LIST_VIEW);
                });
                appBar.setNavIcon(back);
                appBar.getActionItems().addAll(home, store);
                
                if (user == null)
                    user = (User) sessionContent.get("activeId");
                
                if (ticket == null) {
                    Integer ticketId = (Integer) sessionContent.get("ticketId");
                    getTicket(ticketId);
                }
                
                if(!(user instanceof Technician)){
                    btnEditState.setVisible(false);
                    btnStore.setVisible(false);
                    comboTechnician.setEditable(true);
                }
            }
            if (comboTechnician.getItems().isEmpty())
                fillTechniciansCombo();
            loadData();
        });
    }
    
    /**
     * Load the MessageView
     * @throws IOException 
     */
    @FXML
    private void handleButtonSendMessage() throws IOException{
        MobileApplication.getInstance().switchView("MessageView");
    }
    
    /**
     * Dialog to select the state 
     * @throws IOException 
     */
    @FXML
    private void handleButtonEditState() throws IOException{
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label("Seleccione estado"));
        Button btnOpen = new Button("Abierto");
        btnOpen.setPrefWidth(200);
        Button btnInProgress = new Button("Progreso");
        btnInProgress.setPrefWidth(200);
        Button btnBloqued = new Button("Bloqueado");
        btnBloqued.setPrefWidth(200);
        Button btnClosed = new Button("Cerrado");
        btnClosed.setPrefWidth(200);
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
        VBox vBox = new VBox();
        vBox.getChildren().addAll(btnOpen, btnInProgress, btnClosed, btnBloqued);
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        dialog.setContent(vBox);
        dialog.showAndWait();
        updateUIState();
    }

    /**
     * Set technician of the ticket depending of the technician combobox selection
     */
    public void handleComboBoxTechnician(){
        Technician technician = 
                comboTechnician.getSelectionModel().getSelectedItem();
        if (technician.getLogin() != null) {
            ticket.setTechnician(technician); 
        } else {
            ticket.setTechnician(null);
        }
    }
    
    /**
     * updates a ticket
     */
    public void updateTicket() {
        try {
            logic.updateTicket(ticket);
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
    }
    /**
     * Get all the technicians from the database
     * @return List with all the database technician
     */
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
    
    /**
     * Fill technicians combo with all the database technicians
     */
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
    /**
     * Creates an empty technician
     * @return empty technician with name balnk and lasName blank
     */
    private Technician getEmptyTechnician() {
        Technician technician = new Technician();
        technician.setLastName1("");
        technician.setName("");
        return technician;
    }
   
    // Checked
    /**
     * Load the data of the fields in the view
     */
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
            lblCreateDateTicket.setText(formatter.format(ticket.getCreateDate()));                                     
        }
        if(ticket.getEndDate()!=null){
                    
            lblEndDateTicket.setText(formatter.format(ticket.getEndDate()));                
        }
    }

    /**
     * get a ticket with a given ticket id
     * @param ticketId Integer - the ticket id to find the ticket
     */
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

    /**
     * Updates the ui state
     */
    private void updateUIState() {
        lblStateTicket.setText(ticket.getState().name().toLowerCase());
        if (ticket.getState().equals(STATE.CLOSED))
            lblEndDateTicket.setText(formatter.format(new Date()));
    }
}
