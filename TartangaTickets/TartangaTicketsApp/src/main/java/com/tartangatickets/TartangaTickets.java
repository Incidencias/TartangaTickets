package com.tartangatickets;

import com.tartangatickets.views.LoginView;
import com.tartangatickets.views.RecoverPassView;
import com.tartangatickets.views.MainMenuView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class TartangaTickets extends MobileApplication {

    public static final String LOGIN_VIEW = HOME_VIEW;
    public static final String RECOVERPASS_VIEW = "RecoverPassView";
    public static final String MAINMENU_VIEW = "MainMenuView";
    
    @Override
    public void init() {
        addViewFactory(LOGIN_VIEW, () -> new LoginView(LOGIN_VIEW).getView());
        addViewFactory(RECOVERPASS_VIEW, () -> new RecoverPassView(RECOVERPASS_VIEW).getView());
        addViewFactory(MAINMENU_VIEW, () -> new MainMenuView(MAINMENU_VIEW).getView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(TartangaTickets.class.getResource("style.css").toExternalForm());

    }
}
