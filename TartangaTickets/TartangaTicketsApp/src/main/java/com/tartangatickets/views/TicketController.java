package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Iker Jon 
 */
public class TicketController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    
    @FXML
    private View ver_incidencias;    
    @FXML
    private ListView<String> lvLTicket ;
    @FXML
    private ComboBox cbStateLTicket;
    @FXML
    private ComboBox cbTechnicianLTicket;
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSessionContent();
    private User user;
    private List<Ticket> visibleTickets = new ArrayList<>();
    private ObservableList<String> itemTickets;
    private ObservableList<String> itemsState;
    private ObservableList<String> itemsTechnicians;
    
    /**
     * Initializes the controller class.
     */
     
    public void initialize() {
        ver_incidencias.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        ver_incidencias.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                user = (User) sessionContent.get("activeId");
                if (!(user instanceof Technician)) {
                    cbTechnicianLTicket.setVisible(false);
                }   
                fillStateCombo();
                fillTechniciansCombo();
                fillTicketList();    
                
                lvLTicket.getSelectionModel().selectedItemProperty()
                        .addListener(this::handleTicketListSelectionChanged);
            }
        });
    }
    
    private void handleTicketListSelectionChanged(
            ObservableValue observable, String oldValue,String newValue) {
        if(newValue!=null){
            sessionContent.put("ticketId", Integer.parseInt(newValue));
            MobileApplication.getInstance().switchView("TicketDetailView");
        }
    }
    
    @FXML
    private void handleFilterReset(){
        cbStateLTicket.setValue("");
        cbTechnicianLTicket.setValue("");
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
                visibleTickets = getAllTickets();
                Technician technician = 
                    getAllTechnicians().get(t);
                visibleTickets = visibleTickets.
                        stream().filter(p -> p.getTechnician().equals(technician)).
                        collect(Collectors.toList());
            } else {
                visibleTickets = getAllTickets();
            }
        }
        updateTicketList();
    }
    
    
    @FXML
    private void handleFilterTechnician() {
        int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
        if (cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1) {
            Technician technician = 
                    getAllTechnicians().get(t);
            visibleTickets = visibleTickets
                    .stream().filter(p -> p.getTechnician().equals(technician))
                    .collect(Collectors.toList());
        } else {
            if (cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
                visibleTickets = getAllTickets();
                visibleTickets = visibleTickets
                        .stream().filter((Ticket p)->p.getState().name()
                                .equals(cbStateLTicket.getSelectionModel().toString()))
                        .collect(Collectors.toList());
            } else {
                visibleTickets = getAllTickets();
            }
        }
        updateTicketList();
    }
    
    private void fillStateCombo() {
        itemsState = FXCollections.observableArrayList();
        itemsState.addAll(STATE.OPEN.name(), STATE.INPROGRESS.name(), 
                STATE.BLOQUED.name(), STATE.CLOSED.name()
        );          
        cbStateLTicket.getItems().addAll(itemsState);
    }

    private void fillTechniciansCombo() {
        itemsTechnicians = FXCollections.observableArrayList();
        List<Technician> technicians = getAllTechnicians();
        technicians.forEach((technician) -> {
            itemsTechnicians.add(technician.getFullName());
        });
        
        cbTechnicianLTicket.getItems().addAll(itemsTechnicians);
    }
    
    private void fillTicketList() {
        itemTickets = FXCollections.observableArrayList();
        if (user instanceof Technician) {
            visibleTickets = getAllTickets();
        }
        else {
            visibleTickets.addAll(user.getCreatedTickets());
        }
        visibleTickets.forEach((ticket) -> {
            itemTickets.add(ticket.toString());
        });
        lvLTicket.setItems(itemTickets);
    }
    
    private void updateTicketList() {
        itemTickets.clear();
        visibleTickets.forEach((ticket)-> {
            itemTickets.add(ticket.toString());
        });
        
        lvLTicket = new ListView();
        lvLTicket.setItems(itemTickets);
    }
    
    private List<Ticket> getAllTickets() {
        List<Ticket> tickets = null;
        try {
            tickets = logic.findAllTickets();
        } catch (NoTicketException ex) {
            DialogHelper.newInstance("INFO", ex.getMessage());
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return tickets;
    }
    
    private List<Technician> getAllTechnicians() {
        List<Technician> technicians = null;
        try {
            technicians = logic.findAllTechnicians();
        } catch (NoTechnicianException ex) {
            cbTechnicianLTicket.setDisable(true);
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return technicians;
    }
}
