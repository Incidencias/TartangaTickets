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
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author ionut
 */
public class LoginController {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

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
    private void handleButtonAccess(){
        LOGGER.info("Access Action event.");
        try{
            if(this.tfUser.getText().trim().isEmpty() || this.pfPass.getText().trim().isEmpty()){
                Alert alert=new Alert(AlertType.ERROR,"Los campos Usuario y Contraseña no pueden estar vacíos.",ButtonType.OK);
                alert.showAndWait();
            } else {
                User user = logic.authenticate(tfUser.getText(), pfPass.getText());
                if(user.getName().equals(tfUser.getText())){
                    if (user != null) {
                        MobileApplication.getInstance().switchView("MainMenuView");
                        tfUser.setText("");
                        pfPass.setText("");
                        sessionContent.put("activeId", user);
                    } else {
                        Alert alert=new Alert(AlertType.ERROR,"Datos incorrectos.",ButtonType.OK);
                        alert.showAndWait();
                    }
                }else{
                    Alert alert=new Alert(AlertType.ERROR,"El Usuario indicado no existe.",ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }catch(Exception ex){
            LOGGER.info("Aplication error.");
        }
        
    }
    @FXML
    private void handleButtonRecoverpass(){
        LOGGER.info("Recover Password Action event.");
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
