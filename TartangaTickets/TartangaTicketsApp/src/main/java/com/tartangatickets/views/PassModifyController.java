/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.tartangatickets.TartangaTickets;
import static com.tartangatickets.TartangaTickets.MAINMENU_VIEW;
import com.tartangatickets.entities.User;
import com.tartangatickets.exceptions.UserLoginException;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.utils.DialogHelper;
import com.tartangatickets.utils.exceptions.NotSecureException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

/**
 * Handle the password modify window 
 *  
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *      <li><strong>sessionContent:</strong> HasMap from logic</li>
 *      <li><strong>user:</strong> logged user from sessionContent</li>
 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
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
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    private final User user = (User) sessionContent.get("activeId");

    /**
     * First actions when initialize the window
     * -Set up the AppBar
     *
     */
    public void initialize() {
        modificar_pass.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        modificar_pass.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Modificar Contraseña");
                Button back = MaterialDesignIcon.ARROW_BACK.button();
                back.setOnAction(event -> {
                    MobileApplication.getInstance().switchToPreviousView();
                    MobileApplication.getInstance().removeViewFactory(TartangaTickets.PASSMODIFY_VIEW);
                    
                });
                appBar.setNavIcon(back);
            }
        });
    }
    
    /**
     * Modify the password changing it for the old one
     */
    @FXML
    private void handleButtonModify(){
        if(!pfOldPass.getText().trim().isEmpty()&&!pfNewPass.getText().trim().isEmpty()&&!pfRepeatPass.getText().trim().isEmpty()){
            try {
                logic.authenticate(user.getLogin(), pfOldPass.getText());
                if(pfNewPass.getText().equals(pfRepeatPass.getText())) {
                    logic.changePassword(user.getCredential(), pfNewPass.getText());
                    DialogHelper.newInstance("INFO", "Contraseña cambiada");
                    MobileApplication.getInstance().removeViewFactory(TartangaTickets.PASSMODIFY_VIEW);
                    MobileApplication.getInstance().switchView(MAINMENU_VIEW);
                } else {
                    DialogHelper.newInstance("WARNING", NEW_PASSWORD_ERROR);
                }
            } catch (UserLoginException ex) {
                DialogHelper.newInstance("ERROR", ex.getMessage());
            } catch (NotSecureException ex){
                DialogHelper.newInstance("WARNING", ex.getMessage());
            } catch (Exception ex) {
                DialogHelper.newInstance("ERROR", GENERAL_ERROR);
            }
        }else{
            DialogHelper.newInstance("ERROR", "Rellene todos los campos");
        }
    }
}
