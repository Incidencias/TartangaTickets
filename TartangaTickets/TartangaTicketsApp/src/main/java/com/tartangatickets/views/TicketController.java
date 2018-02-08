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
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    private static final Logger logger= Logger.getLogger("views");
    
    private Stage stage;
    
    @FXML
    private View ver_incidencias;
    
    @FXML
    private ListView lvLTicket;
    @FXML
    private ComboBox cbStateLTicket;
    @FXML
    private ComboBox cbTechnicianLTicket;
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    private User user;
    private State state;
    private  List<Ticket> ticketUser;
    private ObservableList<Integer> data;
    private ObservableList<State> itemsState;
    private ObservableList<String> itemsTechnicianN;
    private List<User> allTechnicians; 
    private List<Ticket> ticketsF;
    private int filtro=0;
    private Technician techSelec;
    /**
     * Initializes the controller class.
     */
     
    public void initialize(URL url, ResourceBundle rb) {
        ver_incidencias.showingProperty().addListener((obs, oldValue, newValue) -> {  
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                user = (User) sessionContent.get("activeId");
                try{
                    itemsState = FXCollections.observableArrayList();
                    itemsTechnicianN = FXCollections.observableArrayList();
                    itemsState.addAll(state.OPEN, state.INPROGRESS, state.BLOQUED, state.CLOSED);          
                    
                    for(User tech : logic.findAllUsers()){
                        if(tech instanceof Technician){
                            allTechnicians.add(tech);
                        }
                    }
                    
                    int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
                    techSelec = (Technician) allTechnicians.get(t);
                    for(int i = 0; i<allTechnicians.size(); i++){                 
                        itemsTechnicianN.add(allTechnicians.get(i).getName()+" "+
                        allTechnicians.get(i).getLastName1()+" "+
                        allTechnicians.get(i).getLastName2());                       
                    }
                    cbStateLTicket = new ComboBox<>(itemsState);
                    cbTechnicianLTicket= new ComboBox<>(itemsTechnicianN);
                
                    lvLTicket = new ListView<String>();
                    data = FXCollections.observableArrayList();
                    
                    ticketUser = user.getCreatedTickets();
                                  
                    if(cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
                        ticketsF = ticketUser.stream().filter(p->p.getState().equals(cbStateLTicket.getSelectionModel()))
                                               .collect(Collectors.toList());
                    }
                    else{
                        ticketsF = ticketUser;
                    }
                    if(cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1){
                        ticketsF = ticketsF.stream().filter(p->p.getTechnician().equals(techSelec))
                                               .collect(Collectors.toList());
                    }
                    
                    for(int i=0; i<ticketsF.size(); i++){
                        data.add(ticketsF.get(i).getId());
                    }
                
                    lvLTicket.setItems(data);
                    
                    Ticket tick = (Ticket) lvLTicket.getSelectionModel().getSelectedItems();
                    sessionContent.put("ticketId", tick);
                    logger.info("Going to Ticket detail Action event");
                    MobileApplication.getInstance().switchView("TicketDetailView");
                }catch (Exception ex) {
                    Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    //TODO hacer que al seleccionar una incidencia que vaya a los detalles de esa incidencia
}
