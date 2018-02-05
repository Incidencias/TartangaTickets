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
    
    public void initialize() {
        nuevo_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                
            }
        });
    }
    
    private void handleButtonCreateUser(){
    }
    
}
