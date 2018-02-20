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
    private ObservableList<String> itemTickets;
    private ObservableList<String> itemsState;
    private ObservableList<String> itemsTechnicianN;
    private List<User> allUsers;
    private List<User> allTechnicians = new ArrayList<User>(); 
    private List<Ticket> ticketsF =  new ArrayList<Ticket>();
    private List<Ticket> visibleTickets;
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
                if (!(user instanceof Technician)) {
                    cbTechnicianLTicket.setVisible(false);
                }   
                fillStateCombo();
                fillTechniciansCombo();
                fillTicketList();    
                
                lvLTicket.getSelectionModel().selectedItemProperty().addListener(this::handleTicketListSelectionChanged);
                
                    
                   
            }
        });
    }
    
    private void handleTicketListSelectionChanged(ObservableValue observable, String oldValue,String newValue) {
        if(newValue!=null){
            sessionContent.put("ticketId", Integer.parseInt(newValue));
            MobileApplication.getInstance().switchView("TicketDetailView");

        }
    }
    
    @FXML
    private void handleFilterState() {
        if (cbStateLTicket.getSelectionModel().getSelectedIndex() != -1) {
            visibleTickets = visibleTickets
                        .stream().filter((Ticket p)->p.getState().name()
                                .equals(cbStateLTicket.getSelectionModel().toString()))
                        .collect(Collectors.toList());
        } else {
            int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
            if (cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1) {
                try {
                    visibleTickets = (List<Ticket>) logic.findAllTickets();
                    Technician technician =
                            ((List<Technician>) logic.findAllTechnicians()).get(t);
                    visibleTickets = visibleTickets.
                            stream().filter(p -> p.getTechnician().equals(technician)).
                            collect(Collectors.toList());
                } catch (Exception ex) {
                    Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                visibleTickets = (List<Ticket>) sessionContent.get("tickets");
            }
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
    
    private void fillStateCombo() {
        itemsState = FXCollections.observableArrayList();
        itemsState.addAll(State.OPEN.name(), State.INPROGRESS.name(), State.BLOQUED.name(), State.CLOSED.name());          
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
        itemTickets = FXCollections.observableArrayList();

        if (user instanceof Technician) {
            try {
                tickets = logic.findAllTickets();
                visibleTickets = tickets;
            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            tickets.addAll(user.getCreatedTickets());
        }

        for (int i=0; i<tickets.size(); i++) {
            itemTickets.add(tickets.get(i).toString());
        }
        lvLTicket.setItems(itemTickets);
    }
    
    private void updateTicketList() {
        itemTickets.clear();
        visibleTickets.forEach((ticket)-> {
            itemTickets.add("#"+ticket.toString());
        });
        
        lvLTicket = new ListView();
        lvLTicket.setItems(itemTickets);
    }
}
