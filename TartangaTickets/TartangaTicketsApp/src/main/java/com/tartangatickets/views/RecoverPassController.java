package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class RecoverPassController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    
    @FXML
    private View recuperar_pass;
    @FXML
    private TextField tfUser;
    @FXML 
    private Button btnRecoverPass;

    public void initialize() {
        recuperar_pass.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
            }
        });
    }    
    
    private void handleButtonRecoverPass(){
        
        logger.info("Access Action event.");
        if(this.tfUser.getText().trim().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR,"El campo Usuario no puede estar vacío.",ButtonType.OK);
            alert.showAndWait();
        } else {
            
            MobileApplication.getInstance().switchView("HOME_VIEW");
        }
        
    }
    
}
