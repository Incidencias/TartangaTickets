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
    private User user;
    
    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void initialize() {
        detalles_incidencia.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
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
        });
        
        
    }
    
    
    private void handleButtonSendMessage() throws IOException{
        logger.info("Going to send Message Action event");
        MobileApplication.getInstance().switchView("MessageView");
    }
    
    private void handleButtonEditState() throws IOException{
        int i =0; 
        logger.info("Openning a dialog to change state. ");
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label("Editando el estdo"));
        dialog.setContent(new Label("Asignar un estado a la incidencia: "));
        Button btnOpen = new Button("Abierto");
        Button btnInProgress = new Button("En progreso");
        Button btnBloqued = new Button("Bloqueado");
        Button btnClosed = new Button("Cerrado");
        btnOpen.setOnAction(e -> {
            if(user.getCreatedTickets().get(i).getState().equals(State.OPEN)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                user.getCreatedTickets().get(i).setState(State.OPEN);
                dialog.hide();
            }
            
        });
        btnInProgress.setOnAction(e -> {
            if(user.getCreatedTickets().get(i).getState().equals(State.INPROGRESS)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                user.getCreatedTickets().get(i).setState(State.INPROGRESS);
                dialog.hide();
            }
        });
        btnBloqued.setOnAction(e -> {
            if(user.getCreatedTickets().get(i).getState().equals(State.BLOQUED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                user.getCreatedTickets().get(i).setState(State.BLOQUED);
                dialog.hide();
            }
        });
        btnClosed.setOnAction(e -> {
            if(user.getCreatedTickets().get(i).getState().equals(State.CLOSED)){
                Alert alert = new Alert(AlertType.ERROR, "¡Este estado ya estaba seleccionado!");
                alert.showAndWait();
            }
            else{
                user.getCreatedTickets().get(i).setState(State.CLOSED);
                dialog.hide();
            }
        });
        dialog.getButtons().addAll(btnOpen, btnInProgress, btnBloqued, btnClosed);
        dialog.showAndWait();
    }
    
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
        lblTechnicianTicket.setText(t.getName()+" "+t.getLastName1()+" "+t.getLastName2());
    }
    
    //TODO saber que tipo de usuarios y segun eso esconder los botones
}
