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
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ubuntu
 */
public class Modificar_usuarioController  {

    private static final Logger LOGGER= Logger.getLogger("views");
    
    private Stage stage;
    @FXML
    private Button modifyUser;
    @FXML
    private View modificar_usuario;
    @FXML
    private TextField tfID;
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
        modificar_usuario.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                
            }
        });
    }
    
    private void handleButtonModifyUser(){
    }
}
