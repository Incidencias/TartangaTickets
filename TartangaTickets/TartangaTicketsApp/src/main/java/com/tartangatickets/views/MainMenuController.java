/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.LOGIN_VIEW;
import static com.tartangatickets.TartangaTickets.NEWTICKET_VIEW;
import static com.tartangatickets.TartangaTickets.PASSMODIFY_VIEW;
import static com.tartangatickets.TartangaTickets.TICKET_LIST_VIEW;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class MainMenuController{

    /**
     * Initializes the controller class.
     */
   // private static final Logger LOGGER= Logger.getLogger("views");
    
    @FXML
    private View menu_principal;
    @FXML
    private Button btShowUsers;
    
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    private User user;
   
    
    public void initialize() {
        btShowUsers.setVisible(false);
        menu_principal.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        menu_principal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Menú");
            }
            
            if(user == null)
                user = (User) sessionContent.get("activeId");

            if(user instanceof Technician && ((Technician)user).getIsAdmin()){
                btShowUsers.setVisible(true);
            }
        });
            
        
    }

    @FXML
    private void handleButtonShowTickets() {
        MobileApplication.getInstance().switchView(TICKET_LIST_VIEW);

    }

    @FXML
    private void handleButtonCreateTicket() {
        MobileApplication.getInstance().addViewFactory(NEWTICKET_VIEW, () -> new NewTicketView(NEWTICKET_VIEW).getView());
        MobileApplication.getInstance().switchView(NEWTICKET_VIEW);

    }

    @FXML
    private void handleButtonModifyPass() {
        MobileApplication
                .getInstance()
                .addViewFactory(PASSMODIFY_VIEW, () -> new PassModifyView(PASSMODIFY_VIEW).getView());
        MobileApplication.getInstance().switchView(PASSMODIFY_VIEW);

    }

    @FXML
    private void handleButtonShowUsers() {
        MobileApplication.getInstance().switchView(TartangaTickets.USER_LIST_VIEW);
    }

    @FXML
    private void handleButtonLogOut() {
        sessionContent.remove("activeId");
        user = null;
        MobileApplication.getInstance().switchView(LOGIN_VIEW);
    }
}
