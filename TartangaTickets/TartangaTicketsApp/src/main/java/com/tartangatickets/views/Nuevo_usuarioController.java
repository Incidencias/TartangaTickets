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
    private Button createUser;
    @FXML
    private View nuevo_usuario;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfPass;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellido1;
    @FXML
    private TextField tfApellido2;
    @FXML
    private TextField tfEmail;
    @FXML
    private DropdownButton dbDepartamento;
    @FXML
    private CheckBox cbTecnico;
    private User user;
    private LogicInterface logic = new Logic();
    
    public void initialize() {
        nuevo_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                
            }
        });
    }
    
    private void handleButtonCreateUser(){
        
        Credential credential = new Credential();
        Department department = new Department();
        department = logic.findDepartmentByName(dbDepartamento.getSelectedItem().getText());
        
        credential.setLogin(tfID.getText());
        //TODO CREDENTIAL PASSWORD
        
        if(cbTecnico.isSelected()){
            Technician technician = new Technician();
            
            technician.setName(tfNombre.getText());
            technician.setLastName1(tfApellido1.getText());
            technician.setLastName2(tfApellido2.getText());
            technician.setEmail(tfEmail.getText());
            technician.setDepartment(department);
            
            logic.createTechnician(user);
        }{
            user = new User();
            
            user.setName(tfNombre.getText());
            user.setLastName1(tfApellido1.getText());
            user.setLastName2(tfApellido2.getText());
            user.setEmail(tfEmail.getText());
            user.setDepartment(department);
            
            logic.createUser(user);   
        }
        
    }
    
}
