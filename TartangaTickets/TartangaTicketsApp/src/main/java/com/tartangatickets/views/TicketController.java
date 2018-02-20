package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.State;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoUserException;
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Iker Jon 
 */
public class TicketController {
    
    private static final Logger logger= Logger.getLogger("views ticker controller");
    private static final String GENERAL_ERROR = "Error inesperado.";
    
    @FXML
    private View ver_incidencias;    
    @FXML
    private ListView<String> lvLTicket ;
    @FXML
    private ComboBox cbStateLTicket;
    @FXML
    private ComboBox cbTechnicianLTicket;
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    private User user;
    private State state;
    private List<Ticket> tickets = new ArrayList<Ticket>();
    private ObservableList<String> data;
    private ObservableList<State> itemsState;
    private ObservableList<String> itemsTechnicianN;
    private List<User> allUsers;
    private List<User> allTechnicians = new ArrayList<User>(); 
    private List<Ticket> ticketsF =  new ArrayList<Ticket>();
    private int filtro=0;
    private Technician techSelec;
    /**
     * Initializes the controller class.
     */
     
    public void initialize() {
        ver_incidencias.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                user = (User) sessionContent.get("activeId");
                
                fillStateCombo();
                fillTechniciansCombo();
                fillTicketList();    
                /*
                int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
                techSelec = (Technician) allTechnicians.get(t);
                */

                //lvLTicket = new ListView<String>();
                
                //ticketsF.addAll(tickets);
                /*
                if(cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
                    ticketsF = ticketUser.stream().filter(p->p.getState().equals(cbStateLTicket.getSelectionModel()))
                                           .collect(Collectors.toList());
                }
                else{
                    ticketsF = ticketUser;
                }
                */
                /*
                if(cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1){
                    ticketsF = ticketsF.stream().filter(p->p.getTechnician().equals(techSelec))
                                           .collect(Collectors.toList());
                }
                */
                //add ticket Id to ObservableList<Integer>
                /*
                Ticket tick = (Ticket) lvLTicket.getSelectionModel().getSelectedItems();
                int t = lvLTicket.getSelectionModel().getSelectedIndex();
                
                sessionContent.put("ticketId", tick);
                */
                lvLTicket.getSelectionModel().selectedItemProperty().addListener(this::handleTicketListSelectionChanged);
                
                    
                    /*
                    if(lvLTicket.getSelectionModel().isSelected(t)){
                        logger.info("Going to Ticket detail Action event");  
                        sessionContent.put("ticketId", tick);
                        MobileApplication.getInstance().switchView("TicketDetailView");
                    }           
                    */
            }
        });
    }
    
    private void handleTicketListSelectionChanged(ObservableValue observable, String oldValue,String newValue) {
                if(newValue!=null){
                                sessionContent.put("ticketId", newValue);
                                MobileApplication.getInstance().switchView("TicketDetailView");
                                
                }
    }
    
    @FXML
    private void handleFilterState() {
        
        if(cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
            tickets.addAll(tickets.stream().filter(p->p.getState().equals(cbStateLTicket.getSelectionModel()))
                    .collect(Collectors.toList()));
        }
        updateTicketList();
    }
    
    
    @FXML
    private void handleFilterTechnician() {
        if(cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1){
            int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
            techSelec = (Technician) allTechnicians.get(t);
        
        tickets.addAll(tickets.stream().filter(p->p.getTechnician().equals(techSelec))
                .collect(Collectors.toList()));
        }
        
        updateTicketList();
    }
    //TODO hacer que al seleccionar una incidencia que vaya a los detalles de esa incidencia
    
    private void fillStateCombo() {
        itemsState = FXCollections.observableArrayList();
        itemsState.addAll(state.OPEN, state.INPROGRESS, state.BLOQUED, state.CLOSED);          
        cbStateLTicket.getItems().addAll(itemsState);
    }

    private void fillTechniciansCombo() {
        itemsTechnicianN = FXCollections.observableArrayList();
                
        try{
            for(User tech : logic.findAllUsers()){
                if(tech instanceof Technician){                           
                    allTechnicians.add(tech);
                }
            }

            for(int i = 0; i<allTechnicians.size(); i++){                 
                itemsTechnicianN.add(allTechnicians.get(i).getFullName());                       
            }
            cbTechnicianLTicket.getItems().addAll(itemsTechnicianN);
        } catch (NoUserException ex) {
            Alert alert=new Alert(
                Alert.AlertType.ERROR,
                ex.getMessage(),
                ButtonType.OK
            );
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert=new Alert(
                Alert.AlertType.ERROR,
                GENERAL_ERROR,
                ButtonType.OK
            );
            alert.showAndWait();
        }
    }
    
    private void fillTicketList() {
        data = FXCollections.observableArrayList();

        if (user instanceof Technician) {
            try {
                tickets = logic.findAllTickets();
            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            tickets.addAll(user.getCreatedTickets());
        }

        for (int i=0; i<tickets.size(); i++) {
            data.add(tickets.get(i).toString());
        }
        lvLTicket.setItems(data);
    }
    
    private void updateTicketList() {
        for(int i=0; i<tickets.size(); i++){
            data.add(tickets.get(i).toString());
        }       
        lvLTicket.setItems(data);
    }
}
