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
import static com.tartangatickets.TartangaTickets.LOGIN_VIEW;
import static com.tartangatickets.TartangaTickets.NEWTICKET_VIEW;
import static com.tartangatickets.TartangaTickets.PASSMODIFY_VIEW;
import static com.tartangatickets.TartangaTickets.TICKET_VIEW;
import static com.tartangatickets.TartangaTickets.USER_VIEW;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private View menu_principal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            menu_principal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
            }
        });
        
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
