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
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.NEWUSER_VIEW;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.NoUserException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Shows a list of users registered in the application and shows a option to 
 * create new users
 * 
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class UsersController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";

    @FXML
    private Button newUser;
    @FXML
    private View usuarios;
    @FXML
    private TableView tableUsers;
    @FXML
    private TableColumn tcDepartment;
    @FXML
    private TableColumn tcUser;
    @FXML
    private TableColumn tcLastName;  
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    private List <User> users;
    
        /**
     * First actions when initialize the window
     * -Set up the AppBar
     * -fill users table
     * -set up table properties
     */
    public void initialize() {
        usuarios.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        usuarios.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Usuarios");
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> 
                    MobileApplication.getInstance().switchToPreviousView()
                );
                appBar.setNavIcon(back);
                fillTable();
            }
        });
        
        tcUser.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName1"));
       
        tcDepartment.setCellValueFactory(
                new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {  
        @Override  
            public ObservableValue<String> call(CellDataFeatures<User, String> data){  
                return data.getValue().getDepartment().codeProperty();  
            }  
        });
    }
    
    /**
     * Load NEWUSER_VIEW
     */
    @FXML
    private void handleButtonNewUser(){
        MobileApplication.getInstance().switchView(NEWUSER_VIEW);
    }

    /**
     * fills the table with data of users 'name, last name and department code
     */
    private void fillTable() {
        try {
            users = logic.findAllUsers();
            tableUsers.setItems(FXCollections.observableArrayList(users));
        } catch (NoUserException ex) {
            DialogHelper.newInstance("ERROR", ex.getMessage());
        } catch (Exception ex) {
            DialogHelper.newInstance("ERROR", GENERAL_ERROR);
        }
    }
}
