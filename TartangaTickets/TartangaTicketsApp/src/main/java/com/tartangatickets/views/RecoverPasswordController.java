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
import com.gluonhq.charm.glisten.control.TextField;
import com.tartangatickets.utils.DialogHelper;

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
    
    public void initialize() {
        recuperar_pass.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button());
                //TODO arrowback button function
            }
        });
    }
    @FXML
    private void handleButtonRecoverpass() throws Exception{
        
        logger.info("Recover Password Action event.");
        if(this.tfUser.getText().trim().isEmpty()){
            DialogHelper.newInstance("ERROR",
                    "El campo Usuario no puede estar vacío.");
            MobileApplication.getInstance().switchView("HOME_VIEW");
        } else {
            logic.recoverPassword(tfUser.getText());
            DialogHelper.newInstance("ERROR",
                    "Se ha enviado la nueva contraseña a su correo.");
        }
        
    }
}
