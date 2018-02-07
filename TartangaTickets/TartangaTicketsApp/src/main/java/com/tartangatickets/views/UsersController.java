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
import static com.tartangatickets.TartangaTickets.NEWUSER_VIEW;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ubuntu
 */
public class UsersController {

    private static final Logger LOGGER= Logger.getLogger("views");
    
    private Stage stage;
    @FXML
    private Button newUser;
    @FXML
    private View usuarios;
    @FXML
    private ListView lvUserList;
    private LogicInterface logic = new Logic();
    
    public void initialize() {
        usuarios.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
            }
        });
        
        try {
            lvUserList.setItems(FXCollections.observableArrayList(logic.findAllUsers()));
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void handleButtonNewUser(){
        MobileApplication.getInstance().switchView(NEWUSER_VIEW);
        
    }
}