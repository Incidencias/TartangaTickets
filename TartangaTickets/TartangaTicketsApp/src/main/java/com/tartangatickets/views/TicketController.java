package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.User;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Iker Jon 
 */
public class TicketController {
    
    private static final Logger LOGGER= Logger.getLogger("views");
    
    private Stage stage;
    
    @FXML
    private View ver_incidencias;
    
    @FXML
    private ListView lvTickets;
    
    private User user;
    private  List<Ticket> ticketUser;
    private ObservableList<Integer> data;
    
    /**
     * Initializes the controller class.
     */
     
    public void initialize(URL url, ResourceBundle rb) {
        ver_incidencias.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                lvTickets = new ListView<String>();
        data = FXCollections.observableArrayList();
        ticketUser = user.getCreatedTickets();
        for(int i=0; i<ticketUser.size(); i++){
            data.add(ticketUser.get(i).getId());
        }
        lvTickets.setItems(data);
        lvTickets.getSelectionModel().selectedItemProperty().addListener(
                /*MobileApplication.getInstance().switchView("TicketDetailView")*/);
            }
        });
    }
    //TODO hacer que al seleccionar una incidencia que vaya a los detalles de esa incidencia
}
