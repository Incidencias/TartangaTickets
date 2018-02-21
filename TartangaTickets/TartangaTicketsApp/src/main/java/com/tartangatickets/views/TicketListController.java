package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.State;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoTechnicianException;
import com.tartangatickets.exceptions.NoTicketException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Iker Jon 
 */
public class TicketListController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    
    @FXML
    private View incidencias_charmlist;    
    @FXML
    private CharmListView<Ticket, STATE> charmTickets ;
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSessionContent();
    private User user;
    private List<Ticket> tickets;
    private ObservableList<Ticket> itemTickets;
    private FilteredList<Ticket> filteredList;
    
    /**
     * Initializes the controller class.
     */
     
    public void initialize() {
        incidencias_charmlist.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        charmTickets.setCellFactory(p -> new TicketCell());
        charmTickets.setHeadersFunction(State::getState);
        filteredList = new FilteredList<>(
                FXCollections.observableArrayList(getAllTickets()),
                getTicketPredicate(null));
        incidencias_charmlist.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                appBar.getMenuItems().setAll(buildFilterMenu());
                user = (User) sessionContent.get("activeId"); 
                fillTicketList();    

                charmTickets.selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue obs, Object oldItem, Object newItem) {
                        if (newItem != null) {
                            sessionContent.put("ticketId", ((Ticket) newItem).getId());
                            MobileApplication.getInstance().switchView("TicketDetailView");
                        }
                    }
                });
            }    
        });
    }
    
    private List<MenuItem> buildFilterMenu() {
        final List<MenuItem> menu = new ArrayList<>();

        EventHandler<ActionEvent> menuActionHandler = e -> {
            MenuItem item = (MenuItem) e.getSource();
            State.STATE state = (State.STATE) item.getUserData();
            filteredList.setPredicate(getTicketPredicate(state));
        };

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioMenuItem allTickets = new RadioMenuItem("Todos");
        allTickets.setOnAction(menuActionHandler);
        allTickets.setSelected(true);
        menu.add(allTickets);
        toggleGroup.getToggles().add(allTickets);

        List<STATE> items = Arrays.asList(STATE.OPEN,STATE.INPROGRESS,STATE.CLOSED,STATE.BLOQUED);
        for (STATE state : items) {
            RadioMenuItem item = new RadioMenuItem(state.name());
            item.setUserData(state);
            item.setOnAction(menuActionHandler);
            menu.add(item);
            toggleGroup.getToggles().add(item);
        }

        return menu;
    }
    
    private Predicate<Ticket> getTicketPredicate(State.STATE state) {
        return ticket -> state == null || ticket.getState().name().equals(state.name());
    }
    
    private void fillTicketList() {
        itemTickets = FXCollections.observableArrayList();
        if (user instanceof Technician) {
            tickets = getAllTickets();
        }
        else {
            tickets = user.getCreatedTickets();
        }
        tickets.forEach((ticket) -> {
            itemTickets.add(ticket);
        });
        charmTickets.setItems(itemTickets);
    }
    
    private List<Ticket> getAllTickets() {
        tickets = null;
        try {
            tickets = logic.findAllTickets();
        } catch (NoTicketException ex) {
            DialogHelper.newInstance("INFO", ex.getMessage());
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return tickets;
    }

}
