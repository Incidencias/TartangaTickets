/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.MAINMENU_VIEW;
import static com.tartangatickets.TartangaTickets.TICKET_VIEW;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class PassModifyController {

    @FXML
    private View modificar_pass;
    @FXML
    private TextField tfOldPass;
    @FXML
    private TextField tfNewPass;
    @FXML
    private TextField tfNewPassBis;
    @FXML
    private Button btModify;
    private final LogicInterface logic = TartangaTickets.LOGIC;
    private final HashMap sessionContent = logic.getSessionContent();
    private final User user = (User) sessionContent.get("activeId");

    public void initialize() {
        modificar_pass.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO backbutton

            }
        });
    }
    
    @FXML
    private void handleButtonModify(){
        if(!tfOldPass.getText().isEmpty()&&!tfNewPass.getText().isEmpty()&&!tfNewPass.getText().isEmpty()){
           
            try {
                if(logic.authenticate(user.getCredential().getLogin(), user.getCredential().getPassword())!=null){
                    logic.changePassword(user.getCredential(), tfNewPass.getText());
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Contraseña cambiada"); 
                    alert.showAndWait();              
                    MobileApplication.getInstance().switchView(MAINMENU_VIEW);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Contraseña incorrecta"); 
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error en la aplicación"); 
                alert.showAndWait();
            }
            
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Rellene todos los campos");       
            alert.showAndWait();
        }
    }

}
