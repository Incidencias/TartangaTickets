/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.DropdownButton;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class NewUserController  {
    
    private Stage stage;

    @FXML
    private View nuevo_usuario;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfLastname1;
    @FXML
    private TextField tfLastname2;
    @FXML
    private TextField tfEmail;
    @FXML
    private DropdownButton dbDepartment;
    @FXML
    private CheckBox cbTechnician;
    private User user;
    private LogicInterface logic = new Logic();
    private List<Department> departments;
    
    public void initialize() {
        nuevo_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                
            }
        });
                
        departments = logic.findAllDepartments();
        
        for(Department dpt:departments){
            dbDepartment.getItems().add(new MenuItem(dpt.getName()));
        }
    }
    
    private void handleButtonCreateUser(){
        
        Credential credential = new Credential();
        Department department = new Department();



        if(!tfName.getText().isEmpty()&& !tfLastname1.getText().isEmpty()&&!tfLastname2.getText().isEmpty()){
            
            
            if(cbTechnician.isSelected()){
                user = new Technician();

            }else{
                user = new User();
                 
            }
            department=(Department) departments.stream().filter(c -> c.getName().equals(dbDepartment.getSelectedItem().getText()));
        
            credential.setLogin(tfEmail.getText());
            user.setName(tfName.getText());
            user.setLastName1(tfLastname1.getText());
            user.setLastName2(tfLastname2.getText());
            user.setDepartment(department);
            try {  
                logic.createUser(user);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Datos erroneos");
                DialogPane dialogPane = alert.getDialogPane();
                alert.showAndWait();
            }
        }

        
    }
    
}
