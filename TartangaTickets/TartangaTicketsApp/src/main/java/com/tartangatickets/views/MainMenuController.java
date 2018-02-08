/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.LOGIN_VIEW;
import static com.tartangatickets.TartangaTickets.NEWTICKET_VIEW;
import static com.tartangatickets.TartangaTickets.PASSMODIFY_VIEW;
import static com.tartangatickets.TartangaTickets.TICKET_VIEW;
import static com.tartangatickets.TartangaTickets.USER_VIEW;
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
    
    @FXML
    private View menu_principal;
    @FXML
    private Button btShowUsers;
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private final HashMap sessionContent = logic.getSessionContent();
    
    public void initialize() {
            menu_principal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO backbutton
            }
        });
            User user =(User) sessionContent.get("activeId");
            if(user instanceof Technician && ((Technician)user).getIsAdmin()){
                btShowUsers.setVisible(true);
            }
        
    }

    @FXML
    private void handleButtonShowTickets() {
        MobileApplication.getInstance().switchView(TICKET_VIEW);

    }

    @FXML
    private void handleButtonCreateTicket() {
        MobileApplication.getInstance().switchView(NEWTICKET_VIEW);

    }

    @FXML
    private void handleButtonModifyPass() {
        MobileApplication.getInstance().switchView(PASSMODIFY_VIEW);

    }

    @FXML
    private void handleButtonShowUsers() {
        MobileApplication.getInstance().switchView(USER_VIEW);

    }

    @FXML
    private void handleButtonLogOut() {
        MobileApplication.getInstance().switchView(LOGIN_VIEW);

    }

}
