package com.tartangatickets.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.tartangatickets.TartangaTickets;
import com.tartangatickets.entities.User;
import com.tartangatickets.logic.LogicInterface;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.gluonhq.charm.glisten.control.TextField;
import static com.tartangatickets.TartangaTickets.RECOVERPASS_VIEW;
import com.tartangatickets.exceptions.UserLoginException;
import javafx.scene.control.PasswordField;
import com.tartangatickets.utils.DialogHelper;


/**
 * Handle the login window 
 *  
 *  <ul>
 *      <li><strong>logic:</strong> Get the logic of the program from TartangaTickets</li>
 *      <li><strong>sessionContent:</strong> HasMap from logic</li>

 *  </ul>
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
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
    private final HashMap sessionContent = logic.getSESSION_CONTENT();
    
    /**
     * First actions when initialize the window
     * Set up the AppBar
     */
    public void initialize() {
        login.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        login.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Iniciar Sesión");
            }
        });

    }
    /**
     * Checks if the inserted user and password in the login are correct
     */
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
                pfPass.clear();
                tfUser.setText(null);
                MobileApplication.getInstance().switchView(TartangaTickets.MAINMENU_VIEW);
            } catch (UserLoginException ex) {
                pfPass.clear();
                tfUser.setText(null);
                DialogHelper.newInstance("ERROR",ex.getMessage() );
            } catch (Exception ex) {
                DialogHelper.newInstance("ERROR",GENERAL_ERROR );
            }        
        }
    }
    /**
     * Load the window for recovery pass
     */
    @FXML
    private void handleButtonRecoverpass(){
        pfPass.clear();
        
        MobileApplication
                .getInstance()
                .addViewFactory(RECOVERPASS_VIEW, () -> new RecoverPassView(RECOVERPASS_VIEW).getView());
        
        MobileApplication.getInstance().switchView("RecoverPassView");
    }
}
