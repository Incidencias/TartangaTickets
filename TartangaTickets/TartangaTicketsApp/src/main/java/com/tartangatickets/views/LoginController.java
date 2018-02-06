package com.tartangatickets.views;

import java.io.IOException;
import com.gluonhq.charm.glisten.application.MobileApplication;
//import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    private View primary;
    
    @FXML
    private TextField tfUser;
    
    @FXML
    private TextField tfPass;
    
    @FXML
    private Button btnAccess;
    
    @FXML
    private Button btnRecoverpass;

    public void initialize() {
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
            }
        });
    }
    
    private void handleButtonAccess(){
        logger.info("Access Action event.");
        if(this.tfUser.getText().trim().isEmpty() || this.tfPass.getText().trim().isEmpty()){
            Alert alert=new Alert(AlertType.ERROR,"Los campos Usuario y Contraseña no pueden estar vacíos.",ButtonType.OK);
            alert.showAndWait();
        } else {
            MobileApplication.getInstance().switchView("MainMenuView");
        }
        
    }
    
    private void handleButtonRecoverpass(){
        logger.info("Recover Password Action event.");
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
