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
import com.tartangatickets.entities.User;

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
    private User user;
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
        if(!user.getName().equals(tfUser.getText())){
            Alert alert=new Alert(Alert.AlertType.ERROR,"El Usuario indicado no existe.",ButtonType.OK);
            alert.showAndWait();
        }else{
            if(this.tfUser.getText().trim().isEmpty()){
                Alert alert=new Alert(Alert.AlertType.ERROR,"El campo Usuario no puede estar vacío.",ButtonType.OK);
                alert.showAndWait();
            } else {
                logic.recoverPassword(tfUser.getText());
                Alert alert=new Alert(Alert.AlertType.INFORMATION,"Se ha enviado la nueva contraseña a su correo.",ButtonType.OK);
                alert.showAndWait();
                MobileApplication.getInstance().switchView("HOME_VIEW");
            }
        }
    }
}
