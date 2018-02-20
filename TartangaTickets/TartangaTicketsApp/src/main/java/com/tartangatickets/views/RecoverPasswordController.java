package com.tartangatickets.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.logic.LogicInterface;
import javafx.fxml.FXML;
import com.gluonhq.charm.glisten.control.TextField;
import com.tartangatickets.exceptions.NoUserException;
import org.apache.commons.mail.EmailException;
import com.tartangatickets.utils.DialogHelper;


/**
 *
 * @author ionut
 */
public class RecoverPasswordController {
    
    private static final String EMAIL_ERROR = "Error al enviar email al usuario.";
    private static final String GENERAL_ERROR = "Error inesperado.";
    
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
    private void handleButtonRecoverpass() {
        
        if(this.tfUser.getText().trim().isEmpty()){
            DialogHelper.newInstance("ERROR",
                    "El campo Usuario no puede estar vacío.");
        } else {
            try {
                logic.recoverPassword(tfUser.getText());
                DialogHelper.newInstance("ERROR",
                    "Se ha enviado la nueva contraseña a su correo.");
                MobileApplication.getInstance().switchView("HOME_VIEW");
            } catch (NoUserException ex) {
                DialogHelper.newInstance("ERROR",
                    ex.getMessage());
            } catch (EmailException ex) {
                DialogHelper.newInstance("ERROR",
                    EMAIL_ERROR);
            } catch (Exception ex) {
                DialogHelper.newInstance("ERROR",
                    GENERAL_ERROR);
            }

        }
    }
}
