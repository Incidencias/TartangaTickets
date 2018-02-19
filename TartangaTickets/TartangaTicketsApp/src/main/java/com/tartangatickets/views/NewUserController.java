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
import com.tartangatickets.exceptions.NoDepartmentException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.Reader;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import org.apache.commons.mail.EmailException;


/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class NewUserController  {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    private static final String EMAIL_ERROR = "Error al enviar email al usuario.";
    private static final String EMAIL_VALID_ERROR = "Error en el formato de email.";

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
        } catch (NoDepartmentException ex) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    ex.getMessage(),
                    ButtonType.OK
            ); 
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    GENERAL_ERROR,
                    ButtonType.OK
            ); 
            alert.showAndWait();
        }
        
        for (Department department : departments){
            dbDepartment.getItems().add(new MenuItem(department.getName()));
        }
    }
    
    @FXML
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
        
            if (!Reader.checkValidEmail(tfEmail.getText())) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        EMAIL_VALID_ERROR,
                        ButtonType.OK
                );
                alert.showAndWait();
                return;
            }
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
            } catch (EmailException ex) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        EMAIL_ERROR,
                        ButtonType.OK
                );
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        GENERAL_ERROR,
                        ButtonType.OK
                );
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    "Rellene los campos",
                    ButtonType.OK
            );
            alert.showAndWait();           
        }
    }
}
