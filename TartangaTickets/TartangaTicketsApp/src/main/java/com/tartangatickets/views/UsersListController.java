/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;


import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.NEWUSER_VIEW;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoUserException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Shows a list of users registered in the application and shows a option to 
 * create new users
 * @author ubuntu
 */
public class UsersListController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";

    @FXML
    private Button newUser;
    @FXML
    private View usuarios_charmlist;
    @FXML
    private CharmListView<User, String> charmUsuarios;
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private List <User> users;
    
    public void initialize() {
        usuarios_charmlist.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        
        usuarios_charmlist.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                charmUsuarios.setCellFactory(p -> new UserCell());
                fillList();
            }
        });
    }
    
    @FXML
    private void handleButtonNewUser(){
        MobileApplication.getInstance().switchView(NEWUSER_VIEW);
    }

    /**
     * fills the table with data of users 'name, last name and department code
     */
    private void fillList() {
        try {
            users = logic.findAllUsers();
            charmUsuarios.setItems(FXCollections.observableArrayList(users));
        } catch (NoUserException ex) {
            DialogHelper.newInstance("INFO", ex.getMessage());
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
    }
}
