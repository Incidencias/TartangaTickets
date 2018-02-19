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
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.UserLoginException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.exceptions.NotSecureException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author Sergio
 */
public class PassModifyController {
    
    private static final String GENERAL_ERROR = "Error inesperado.";
    private static final String NEW_PASSWORD_ERROR = "Contraseñas no iguales.";
    
    @FXML
    private View modificar_pass;
    @FXML
    private PasswordField pfOldPass;
    @FXML
    private PasswordField pfNewPass;
    @FXML
    private PasswordField pfRepeatPass;
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
        if(!pfOldPass.getText().trim().isEmpty()&&!pfNewPass.getText().trim().isEmpty()&&!pfRepeatPass.getText().trim().isEmpty()){
            try {
                logic.authenticate(user.getCredential().getLogin(), pfOldPass.getText());
                if(pfNewPass.getText().equals(pfRepeatPass.getText())) {
                    logic.changePassword(user.getCredential(), pfNewPass.getText());
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Contraseña cambiada",ButtonType.OK); 
                    alert.showAndWait();              
                    MobileApplication.getInstance().switchView(MAINMENU_VIEW);
                } else {
                    Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    NEW_PASSWORD_ERROR,
                    ButtonType.OK);       
                    alert.showAndWait();
                }
                
            } catch (UserLoginException ex) {
                Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    ex.getMessage(),
                    ButtonType.OK);       
                alert.showAndWait();
            } catch (NotSecureException ex){
                Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    ex.getMessage(),
                    ButtonType.OK);       
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    GENERAL_ERROR,
                    ButtonType.OK);       
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(
                    Alert.AlertType.ERROR, 
                    "Rellene todos los campos",
                    ButtonType.OK);       
            alert.showAndWait();
        }
    }
}
