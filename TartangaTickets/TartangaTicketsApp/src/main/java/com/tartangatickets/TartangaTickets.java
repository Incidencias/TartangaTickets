package com.tartangatickets;

import com.tartangatickets.views.PrimaryView;
import com.tartangatickets.views.SecondaryView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.tartangatickets.views.Modificar_usuarioView;
import com.tartangatickets.views.Nuevo_usuarioView;
import com.tartangatickets.views.UsuariosView;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class TartangaTickets extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String MENU_LAYER = "Side Menu";
    
    @Override
    public void init() {
        addViewFactory(PRIMARY_VIEW, () -> new UsuariosView(PRIMARY_VIEW).getView());
        addViewFactory(PRIMARY_VIEW, () -> new Modificar_usuarioView(PRIMARY_VIEW).getView());
        addViewFactory(PRIMARY_VIEW, () -> new Nuevo_usuarioView(PRIMARY_VIEW).getView());
       
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(TartangaTickets.class.getResource("style.css").toExternalForm());

    }
}
