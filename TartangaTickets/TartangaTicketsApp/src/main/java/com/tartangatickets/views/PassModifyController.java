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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class PassModifyController {
    
    private static final Logger logger = Logger.getLogger("Window PassModifyController");
    
    @FXML
    private View modificar_pass;
    @FXML
    private TextField pfOldPass;
    @FXML
    private TextField pfNewPass;
    @FXML
    private TextField pfRepeatPass;
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
                logger.info(user.getCredential().getLogin()+" "+user.getCredential().getPassword());

            }
        });
    }
    
    @FXML
    private void handleButtonModify(){
        if(!pfOldPass.getText().isEmpty()&&!pfNewPass.getText().isEmpty()&&!pfNewPass.getText().isEmpty()){
            try {
                User userp= logic.authenticate(user.getCredential().getLogin(), user.getCredential().getPassword());
                logger.info(userp.getLogin());
            } catch (Exception ex) {
                Logger.getLogger(PassModifyController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            try {
                if(logic.authenticate(user.getCredential().getLogin(), user.getCredential().getPassword())!=null){
                    logic.changePassword(user.getCredential(), pfNewPass.getText());
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
