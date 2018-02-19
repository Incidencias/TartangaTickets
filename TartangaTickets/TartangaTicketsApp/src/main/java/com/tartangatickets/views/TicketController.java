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
import java.util.ArrayList;
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
    
    private static final Logger logger= Logger.getLogger("views ticker controller");
    
    @FXML
    private View ver_incidencias;    
    @FXML
    private ListView lvLTicket = new ListView<String>();
    @FXML
    private ComboBox cbStateLTicket;
    @FXML
    private ComboBox cbTechnicianLTicket;
    
    private LogicInterface logic = TartangaTickets.LOGIC; 
    private HashMap sessionContent = logic.getSessionContent();
    private User user;
    private State state;
    private List<Ticket> ticketUser = new ArrayList<Ticket>();
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
                logger.info("inizialize ticket controller");
                user = (User) sessionContent.get("activeId");
                try{
                    itemsState = FXCollections.observableArrayList();
                    itemsTechnicianN = FXCollections.observableArrayList();
                    itemsState.addAll(state.OPEN, state.INPROGRESS, state.BLOQUED, state.CLOSED);          
                    
                    logger.info("tamaño del itemsState: "+itemsState.size());
                    
                    for(User tech : logic.findAllUsers()){
                        if(tech instanceof Technician){                           
                            allTechnicians.add(tech);
                        }
                    }
                    
                    
                    for(int i = 0; i<allTechnicians.size(); i++){                 
                        itemsTechnicianN.add(allTechnicians.get(i).getName()+" "+
                        allTechnicians.get(i).getLastName1()+" "+
                        allTechnicians.get(i).getLastName2());                       
                    }
                    
                    
                    
                    logger.info("tamaño del itemsState: "+itemsState.size());
                    logger.info("tamaño del itemsTechnicianN: "+itemsTechnicianN.size());
                    
                    cbStateLTicket.getItems().addAll(itemsState);
                    cbTechnicianLTicket.getItems().addAll(itemsTechnicianN);
                    
                   
                    /*
                    int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
                    techSelec = (Technician) allTechnicians.get(t);
                    */
                    
                    
                    //lvLTicket = new ListView<String>();
                    data = FXCollections.observableArrayList();
                    
                    if(user instanceof Technician){
                        allUsers = logic.findAllUsers();
                        for(int g=0; g<allUsers.size(); g++){
                            for(int s=0; s<allUsers.get(g).getCreatedTickets().size(); s++){
                                logger.info(allUsers.get(g).getCreatedTickets().get(s).toString());
                                ticketUser.add(allUsers.get(g).getCreatedTickets().get(s));
                            }
                        }
                    }
                    else{
                        ticketUser.addAll(user.getCreatedTickets());
                    }
                   
                    
                    logger.info("Add all tickets to ticketUser. ");
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
                    ticketsF.addAll(ticketUser);
                    for(int i=0; i<ticketsF.size(); i++){
                        data.add(ticketsF.get(i).toString());
                    }
                    logger.info("tamaño del data"+data.size());
                    lvLTicket.setItems(data);
                    
                    Ticket tick = (Ticket) lvLTicket.getSelectionModel().getSelectedItems();
                    sessionContent.put("ticketId", tick);
                    lvLTicket.getSelectionModel().selectedItemProperty().addListener(
                        MobileApplication.getInstance().switchView("TicketDetailView"));
                    
                    /*
                    if(lvLTicket.getSelectionModel().isSelected(t)){
                        logger.info("Going to Ticket detail Action event");  
                        sessionContent.put("ticketId", tick);
                        MobileApplication.getInstance().switchView("TicketDetailView");
                    }           
                    */
                }catch (Exception ex) {
                    Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @FXML
    private void handleFilterState() {
        
        if(cbStateLTicket.getSelectionModel().getSelectedIndex()!=-1){
                        ticketsF.addAll(ticketsF.stream().filter(p->p.getState().equals(cbStateLTicket.getSelectionModel()))
                                               .collect(Collectors.toList()));
        }
        for(int i=0; i<ticketsF.size(); i++){
            data.add(ticketsF.get(i).toString());
        }
        lvLTicket = new ListView<String>();        
        lvLTicket.setItems(data);
    }
    
    @FXML
    private void handleFilterTechnician() {
        
        int t = cbTechnicianLTicket.getSelectionModel().getSelectedIndex();
        techSelec = (Technician) allTechnicians.get(t);
        if(cbTechnicianLTicket.getSelectionModel().getSelectedIndex()!=-1){
                        ticketsF.addAll(ticketsF.stream().filter(p->p.getTechnician().equals(techSelec))
                                               .collect(Collectors.toList()));
        }
        
        for(int i=0; i<ticketsF.size(); i++){
            data.add(ticketsF.get(i).toString());
        }
        lvLTicket = new ListView<String>();
        lvLTicket.setItems(data);
    }
    //TODO hacer que al seleccionar una incidencia que vaya a los detalles de esa incidencia
}
