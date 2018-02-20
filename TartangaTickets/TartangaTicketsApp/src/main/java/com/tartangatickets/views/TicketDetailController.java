package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.State;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    @FXML
    private Button btnEditState;
    @FXML
    private Button btnEditTechnician;
    
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    private User user;
    private Ticket ticket;
    private Date endDate;
    private int i = 0; 
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void initialize() {
        detalles_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                user = (User) sessionContent.get("activeId");
                ticket = (Ticket) sessionContent.get("ticketId");
                
                if(user instanceof Technician){
                    btnEditState.setVisible(true);
                    btnEditTechnician.setVisible(true);
                }
                else{
                    btnEditState.setVisible(false);
                    btnEditTechnician.setVisible(false);
                }
                
                for(int j=0; j<user.getCreatedTickets().size(); j++){
                    if(user.getCreatedTickets().get(j).getId().equals(ticket.getId())){
                        i=j;
                        break;
                    }
                }
                lblIdTicket.setText(ticket.getId().toString());
                lblUserTicket.setText(ticket.getUser().toString());
                lblTechnicianTicket.setText(ticket.getTechnician().getName()+" "
                    +ticket.getTechnician().getLastName1()+" "+ticket.getTechnician().getLastName2());
                lblDepartmentTicket.setText(ticket.getDepartment().getName());
                lblLocationTicket.setText(ticket.getLocation());
                lblMachineCodeTicket.setText(ticket.getMachineCode());
                lblStateTicket.setText(ticket.getState().toString());
                lblCreateDateTicket.setText(formato.format(ticket.getCreateDate()));
                lblEndDateTicket.setText(formato.format(ticket.getEndDate()));
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
            if(user.getCreatedTickets().get(i).getState().equals(State.OPEN)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    user.getCreatedTickets().get(i).setState(State.OPEN);
                    endDate = null;
                    user.getCreatedTickets().get(i).setEndDate(endDate);
                    logic.changeState(user.getCreatedTickets().get(i));
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
            
        });
        btnInProgress.setOnAction(e -> {
            logger.info("Dialog Edit state In progress");
            if(user.getCreatedTickets().get(i).getState().equals(State.INPROGRESS)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    user.getCreatedTickets().get(i).setState(State.INPROGRESS);
                    endDate = null;
                    user.getCreatedTickets().get(i).setEndDate(endDate);
                    logic.changeState(user.getCreatedTickets().get(i));
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        btnBloqued.setOnAction(e -> {
            logger.info("Dialog Edit state Bloqued");
            if(user.getCreatedTickets().get(i).getState().equals(State.BLOQUED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                try {
                    user.getCreatedTickets().get(i).setState(State.BLOQUED);
                    endDate = null;
                    user.getCreatedTickets().get(i).setEndDate(endDate);
                    logic.changeState(user.getCreatedTickets().get(i));
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        btnClosed.setOnAction(e -> {
            logger.info("Dialog Edit state Close");
            if(user.getCreatedTickets().get(i).getState().equals(State.CLOSED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{   
                try {
                    endDate = new Date();
                    user.getCreatedTickets().get(i).setState(State.CLOSED);
                    user.getCreatedTickets().get(i).setEndDate(endDate);
                    logic.changeState(user.getCreatedTickets().get(i));
                } catch (Exception ex) {
                    Logger.getLogger(TicketDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.hide();
            }
        });
        dialog.getButtons().addAll(btnOpen, btnInProgress, btnBloqued, btnClosed);
        dialog.showAndWait();
    }
    @FXML
    private void handleButtonEditTechnician() throws IOException, Exception{
        logger.info("Openning a dialog to change technician. ");
        ListView lvTechnician = new ListView<User>();
        List<User> allUser;
        allUser = logic.findAllUsers();
        ObservableList<User> data;
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label("Cambiando el tecnico"));
        dialog.setContent(lvTechnician);
        
        data = FXCollections.observableArrayList();
        for(int j=0; j<allUser.size(); j++){
            if(allUser.get(j) instanceof Technician){
                data.add(allUser.get(j));
            }
        }
        lvTechnician.setItems(data);
        Technician t = (Technician) lvTechnician.getSelectionModel().getSelectedItems();
        user.getCreatedTickets().get(i).setTechnician(t);
        logic.assignTicket(user.getCreatedTickets().get(i));
        lblTechnicianTicket.setText(t.getName()+" "+t.getLastName1()+" "+t.getLastName2());
        
    }
    
    //TODO saber que tipo de usuarios y segun eso esconder los botones, fomato de las fechas
}
