package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.logic.LogicInterface;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 *
 * @author ionut
 */
public class RecoverPasswordController {
    private static final Logger logger = Logger.getLogger(RecoverPasswordController.class.getName());
    
    @FXML
    private View recuperar_pass;
    @FXML
    private TextField tfUser;
    private final LogicInterface logic = TartangaTickets.LOGIC;
    private final String username = tfUser.getText();
    
    public void initialize() {
        recuperar_pass.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO arrowback button function
            }
        });
    }
    
    private void handleButtonRecoverpass() throws Exception{
        
        logger.info("Recover Password Action event.");
        if(this.username.trim().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR,"El campo Usuario no puede estar vacío.",ButtonType.OK);
            alert.showAndWait();
        } else {
            logic.recoverPassword(username);
            Alert alert=new Alert(Alert.AlertType.INFORMATION,"Se ha enviado la nueva contraseña a su correo.",ButtonType.OK);
            alert.showAndWait();
        }
        MobileApplication.getInstance().switchView("HOME_VIEW");
    }
}
