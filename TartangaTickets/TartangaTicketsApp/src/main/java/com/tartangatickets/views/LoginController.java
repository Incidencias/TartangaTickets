package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import com.gluonhq.charm.glisten.control.TextField;
import com.tartangatickets.exceptions.UserLoginException;
import javafx.scene.control.PasswordField;
import com.tartangatickets.utils.DialogHelper;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class LoginController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";

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
        if(this.tfUser.getText().trim().isEmpty() || 
                this.pfPass.getText().trim().isEmpty()){
            DialogHelper.newInstance("ERROR","Introduzca usuario y contraseña.");
        } else {
            User user = null;
            try {
                user = logic.authenticate(tfUser.getText(), pfPass.getText());

                sessionContent.put("activeId", user);
                MobileApplication.getInstance().switchView("MainMenuView");
            } catch (UserLoginException ex) {
                DialogHelper.newInstance("ERROR",ex.getMessage() );
            } catch (Exception ex) {
                DialogHelper.newInstance("ERROR",GENERAL_ERROR );
            }        

        }
    }
    @FXML
    private void handleButtonRecoverpass(){
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
