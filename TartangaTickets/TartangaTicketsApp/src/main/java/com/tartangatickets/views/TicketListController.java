package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.TICKETDETAIL_VIEW;
import com.tartangatickets.entities.State;
import com.tartangatickets.entities.State.STATE;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoTicketException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * Handle the ticket details window 
 *  
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *      <li><strong>sessionContent:</strong> HasMap from logic</li> 
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class TicketListController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    
    @FXML
    private View incidencias_charmlist;    
    @FXML
    private CharmListView<Ticket, STATE> charmTickets ;
    @FXML VBox vbMain;
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    private User user;
    private FilteredList<Ticket> filteredList;
     
    /**
     * First actions when initialize the window
     * -Set up the AppBar
     * -Get the logged user
     * -Fill ticket list
     */
    public void initialize() {
        incidencias_charmlist.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        charmTickets.setCellFactory(p -> new TicketCell());
        charmTickets.setHeadersFunction(State::getState);
        
        incidencias_charmlist.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Incidencias");
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> {
                    MobileApplication.getInstance().switchView(TartangaTickets.MAINMENU_VIEW);
                });
                appBar.setNavIcon(back);
                appBar.getMenuItems().setAll(buildFilterMenu());
                user = (User) sessionContent.get("activeId"); 

                fillTicketList();    

                //Creates a listener of the charm list

                charmTickets.selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue obs, Object oldItem, Object newItem) {
                        if (newItem != null) {
                            //set up ticket id the select ticket
                            sessionContent.put("ticketId", ((Ticket) newItem).getId());
                            //Goes to TICKETDETAIL_VIEW
                            MobileApplication.getInstance().switchView(TICKETDETAIL_VIEW);
                        }
                    }
                });
            } 
        });
        
        FloatingActionButton fab = new FloatingActionButton(
                MaterialDesignIcon.ADD.text,
                e -> {
                    MobileApplication
                            .getInstance()
                            .addViewFactory(TartangaTickets.NEWTICKET_VIEW, () -> 
                                    new NewTicketView(TartangaTickets.NEWTICKET_VIEW).getView());
                    MobileApplication.getInstance().switchView(TartangaTickets.NEWTICKET_VIEW);
                });
        incidencias_charmlist.getLayers().add(fab.getLayer());
    }
    
    /**
     * Filter menu of tickets using the state
     * @return 
     */
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
    
    /**
     * Gets the predicate
     * @param state Predicate of Ticket
     * @return 
     */
    private Predicate<Ticket> getTicketPredicate(State.STATE state) {
        return ticket -> state == null || ticket.getState().name().equals(state.name());
    }
    
    /**
     * fill the ticket list 
     *  -with all the tickets if the logged user is a technician
     *  -with logged user created tickets if its a normal user
     */
    private void fillTicketList() {
        List<Ticket> tickets = null;
        if (user instanceof Technician) {
            tickets = getAllTickets();
        } else {
            tickets = user.getCreatedTickets();
        }
        if (tickets == null) return;
        filteredList = new FilteredList<>(
                FXCollections.observableArrayList(tickets),
                getTicketPredicate(null));
        charmTickets.setItems(filteredList);
    }
    /**
     * Gets all the tickets from the database
     * @return list with all the tickets
     */
    private List<Ticket> getAllTickets() {
        List<Ticket> tickets = null;
        try {
            tickets = logic.findAllTickets();
        } catch (NoTicketException ex) {
            
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
        return tickets;
    }
}
