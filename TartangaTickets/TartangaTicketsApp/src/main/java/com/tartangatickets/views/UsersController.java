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
import static com.tartangatickets.TartangaTickets.NEWUSER_VIEW;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class UsersController {

    private static final Logger LOGGER= Logger.getLogger("views");
    
    private Stage stage;
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
    
    public void initialize() {
        usuarios.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                                //TODO backbutton

            }
        });
        
        fillTable();

        
    }
    
    @FXML
    private void handleButtonNewUser(){
        MobileApplication.getInstance().switchView(NEWUSER_VIEW);
        
    }

    private void fillTable() {
                //TODO put department
       // tcDepartment.setCellValueFactory(new PropertyValueFactory<>("nSeguimiento"));       
        tcUser.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName1"));
        tcDepartment.setCellValueFactory(new Callback<CellDataFeatures<User, String>, 
                                                         ObservableValue<String>>() {  
        @Override  
          public ObservableValue<String> call(CellDataFeatures<User, String> data){  
         return data.getValue().getDepartment().codeProperty();  
         }  
        });  
        
        try {
            tableUsers.setItems(FXCollections.observableArrayList(logic.findAllUsers()));
            
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al rellenar la tabla"); 
            alert.showAndWait();
        }
    }
}
