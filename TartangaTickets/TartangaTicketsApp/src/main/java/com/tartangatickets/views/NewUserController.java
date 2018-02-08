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
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.Credential;
import com.tartangatickets.entities.Department;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class NewUserController  {

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
    private List<Department> departments;
    private final LogicInterface logic = TartangaTickets.LOGIC; 
    
    public void initialize() {
        nuevo_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                
            }
        });
                
        try {
            departments = logic.findAllDepartments();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error en la aplicación"); 
            alert.showAndWait();
        }
        
        for(Department dpt:departments){
            dbDepartment.getItems().add(new MenuItem(dpt.getName()));
        }
    }
    
    private void handleButtonCreateUser(){
        
        Credential credential = new Credential();
        Department department = new Department();



        if(!tfName.getText().isEmpty()&& !tfLastname1.getText().isEmpty()&&!tfLastname2.getText().isEmpty()&&!tfEmail.getText().isEmpty()){
            
            
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
                tfName.setText("");
                tfLastname1.setText("");
                tfLastname2.setText("");
                tfEmail.setText("");
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Datos erroneos");
                alert.showAndWait();
            }
        }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Rellene los datos");
                alert.showAndWait();           
        }

        
    }
    
}
