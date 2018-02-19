package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import com.gluonhq.charm.glisten.control.TextField;
import java.util.logging.Level;
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
    
    public void initialize() {
        
        login.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO arrowback button function
            }
        });
    }
    
    @FXML
    private void handleButtonAccess() {
        
        logger.info("Access Action event.");
        if(this.tfUser.getText().trim().isEmpty() || this.pfPass.getText().trim().isEmpty()){
            Alert alert=new Alert(AlertType.ERROR,"Los campos Usuario y Contraseña no pueden estar vacíos.",ButtonType.OK);
            alert.showAndWait();
        } else {
            User user = null;
            
            try {
                user = logic.authenticate(tfUser.getText(), pfPass.getText());
                System.out.println(user == null ? "Vacio" : "Lleno");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            if (user != null) {
                MobileApplication.getInstance().switchView("MainMenuView");
                sessionContent.put("activeId", user);
            } else {
                Alert alert=new Alert(AlertType.ERROR,"Datos incorrectos.",ButtonType.OK);
                alert.showAndWait();
            }
        }
        
    }
    @FXML
    private void handleButtonRecoverpass(){
        logger.info("Recover Password Action event.");
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
