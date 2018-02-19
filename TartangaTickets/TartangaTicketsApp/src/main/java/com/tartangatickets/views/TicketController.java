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
import com.tartangatickets.exceptions.NoTicketException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.exceptions.NoTechnicianException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Iker Jon 
 */
public class TicketController {
    
    private static final int VISIBLE_ROW = 5;
    
    @FXML
    private View ver_incidencias;    
    @FXML
    private ListView lvLTicket = new ListView<String>();
    @FXML
    private ComboBox cbStateLTicket;
    @FXML
    private ComboBox cbTechnicianLTicket;
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSessionContent();
    private User user;
    List<Ticket> visibleTickets;
    private ObservableList<String> itemTickets;
    private ObservableList<State> itemsState;
    private ObservableList<String> itemsTechnicianN;
     
    public void initialize() {
        ver_incidencias.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
            }
        });
        user = (User) sessionContent.get("activeId");
        sessionContent.put("tickets", getAllTickets());
        sessionContent.put("technicians", getAllTechnicians());
        cbStateLTicket.setVisibleRowCount(VISIBLE_ROW);
        cbTechnicianLTicket.setVisibleRowCount(VISIBLE_ROW);
        if (!(user instanceof Technician)) {
            cbTechnicianLTicket.setVisible(false);
        }
        fillStateCombo();
        fillTechniciansCombo();
        fillTicketList();
        
        lvLTicket.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String idString = (String) lvLTicket.getSelectionModel().getSelectedItem();
                Integer id = Integer.parseInt(idString);
                sessionContent.put("ticketId", id);
                sessionContent.remove("technicians");
                sessionContent.remove("tickets");
                MobileApplication.getInstance().switchView("TicketDetailView");
            }
        });
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
                visibleTickets = (List<Ticket>) sessionContent.get("tickets");
                Technician technician = 
                    ((List<Technician>) sessionContent.get("technicians")).get(t);
                visibleTickets = visibleTickets.
                        stream().filter(p -> p.getTechnician().equals(technician)).
                        collect(Collectors.toList());
            } else {
                visibleTickets = (List<Ticket>) sessionContent.get("tickets");
            }
        }
        updateTicketList();
    }

    @FXML
    private void handleFilterTechnician() {
        int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
        if (cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1) {
            Technician technician = 
                    ((List<Technician>) sessionContent.get("technicians")).get(t);
            visibleTickets = visibleTickets.
                    stream().filter(p -> p.getTechnician().equals(technician)).
                    collect(Collectors.toList());
        } else {
            if (cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
                visibleTickets = (List<Ticket>) sessionContent.get("tickets");
                visibleTickets = visibleTickets
                        .stream().filter((Ticket p)->p.getState().name()
                                .equals(cbStateLTicket.getSelectionModel().toString()))
                        .collect(Collectors.toList());
            } else {
                visibleTickets = (List<Ticket>) sessionContent.get("tickets");
            }
        }
        updateTicketList();
    }
    
    private void updateTicketList() {
        itemTickets.clear();
        visibleTickets.forEach((ticket)-> {
            itemTickets.add("#"+ticket.toString());
        });
        
        lvLTicket = new ListView();
        lvLTicket.setItems(itemTickets);
    }
    
    private List<Ticket> getAllTickets() {
        List<Ticket> tickets = null;
        try {
            tickets = logic.findAllTickets();
        } catch (NoTicketException ex) {
        
        } catch (Exception ex) {
            
        }
        return tickets;
    }
    
    private void fillTechniciansCombo() {
        itemsTechnicianN = FXCollections.observableArrayList();
        List<Technician> technicians = (List<Technician>) sessionContent.get("technicians");
        technicians.forEach((technician) -> {
            itemsTechnicianN.add(technician.getFullName());
        });
        
        cbTechnicianLTicket.getItems().addAll(itemsTechnicianN);
    }

    private void fillStateCombo() {
        itemsState = FXCollections.observableArrayList();
        itemsState.addAll(State.OPEN, State.INPROGRESS, State.BLOQUED, State.CLOSED);
        cbStateLTicket.getItems().addAll(itemsState);
    }
    
    private void fillTicketList() {
        itemTickets = FXCollections.observableArrayList();
        List <Ticket> tickets = null;
        try {
            if (user instanceof Technician) {
                tickets = (List<Ticket>) sessionContent.get("tickets");
                visibleTickets = tickets;
            } else {
                tickets = user.getCreatedTickets();
                visibleTickets = tickets;
            }
            for (Ticket ticket : tickets) {
                itemTickets.add("#" + ticket.toString());
            }
            lvLTicket.setItems(itemTickets);
        } catch (Exception ex) {
            
        }
    }

    private Object getAllTechnicians() {
        List<Technician> technicians = null;
        try {
            technicians = logic.findAllTechnicians();
        } catch (NoTechnicianException ex) {
        
        } catch (Exception ex) {
            
        }
        return technicians;
    }
}
