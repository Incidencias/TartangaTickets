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
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ubuntu
 */
public class Nuevo_usuarioController  {

    private static final Logger LOGGER= Logger.getLogger("views");
    
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
    
    public void initialize() {
        nuevo_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                
            }
        });
    }
    
    private void handleButtonCreateUser(){
        
        Credential credential = new Credential();
        Department department = new Department();
        department = logic.findDepartmentByName(dbDepartment.getSelectedItem().getText());
        
        credential.setLogin(tfEmail.getText());
        //TODO CREDENTIAL PASSWORD
        if(!tfName.getText().isEmpty()&& !tfLastname1.getText().isEmpty()&&!tfLastname2.getText().isEmpty()){
            if(cbTechnician.isSelected()){
                Technician technician = new Technician();

                technician.setName(tfName.getText());
                technician.setLastName1(tfLastname1.getText());
                technician.setLastName2(tfLastname2.getText());
                technician.setDepartment(department);

                logic.createTechnician(user);
            }{
                user = new User();

                user.setName(tfName.getText());
                user.setLastName1(tfLastname1.getText());
                user.setLastName2(tfLastname2.getText());
                user.setDepartment(department);

                logic.createUser(user);   
            }
        }

        
    }
    
}
