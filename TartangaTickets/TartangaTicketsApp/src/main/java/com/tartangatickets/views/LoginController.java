package com.tartangatickets.views;

import java.io.IOException;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.entities.Credential;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    private View login;
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPass;
    @FXML
    private Button btnAccess;
    @FXML
    private Button btnRecoverpass;
    private final LogicInterface logic = TartangaTickets.LOGIC;
    private final HashMap sessionContent = logic.getSessionContent();
    private final String username = tfUser.getText();
    private final String pass = pfPass.getText();
    
    public void initialize() {
        
        login.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO arrowback button function
            }
        });
    }
    
    private void handleButtonAccess() throws Exception{
        
        logger.info("Access Action event.");
        if(this.username.trim().isEmpty() || this.pass.trim().isEmpty()){
            Alert alert=new Alert(AlertType.ERROR,"Los campos Usuario y Contraseña no pueden estar vacíos.",ButtonType.OK);
            alert.showAndWait();
        } else {
            User user = logic.authenticate(username, pass);
            if (user != null) {
                MobileApplication.getInstance().switchView("MainMenuView");
                sessionContent.put("activeId", user);
            } else {
                Alert alert=new Alert(AlertType.ERROR,"Datos incorrectos.",ButtonType.OK);
                alert.showAndWait();
            }
        }
        
    }
    
    private void handleButtonRecoverpass(){
        logger.info("Recover Password Action event.");
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
