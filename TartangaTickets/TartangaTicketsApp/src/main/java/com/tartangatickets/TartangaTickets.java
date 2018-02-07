package com.tartangatickets;

import com.tartangatickets.views.LoginView;
import com.tartangatickets.views.RecoverPassView;
import com.tartangatickets.views.MainMenuView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.views.Modificar_usuarioView;
import com.tartangatickets.views.Nuevo_usuarioView;
import com.tartangatickets.views.TicketView;
import com.tartangatickets.views.UsuariosView;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class TartangaTickets extends MobileApplication {

    public static final String LOGIN_VIEW = HOME_VIEW;
    public static final String RECOVERPASS_VIEW = "RecoverPassView";
    public static final String MAINMENU_VIEW = "MainMenuView";
    public static final String USER_VIEW = "UserView";
    public static final String USERMODIFY_VIEW = "UserModifyView";
    public static final String NEWUSER_VIEW = "NewUserView";
    public static final String MESSAGE_VIEW = "MessageView";
    public static final String TICKETDETAIL_VIEW = "TicketDetailView";
    public static final String NEWTICKET_VIEW = "NewTicketView";
    public static final String PASSMODIFY_VIEW = "PassModifyView";
    public static final String TICKET_VIEW = "TicketView";
    public static final LogicInterface LOGIC = new Logic();
    
    @Override
    public void init() {
        addViewFactory(LOGIN_VIEW, () -> new LoginView(LOGIN_VIEW).getView());
        addViewFactory(RECOVERPASS_VIEW, () -> new RecoverPassView(RECOVERPASS_VIEW).getView());
        addViewFactory(MAINMENU_VIEW, () -> new MainMenuView(MAINMENU_VIEW).getView());
        addViewFactory(USER_VIEW, () -> new UsuariosView(USER_VIEW).getView());
        addViewFactory(USERMODIFY_VIEW, () -> new Modificar_usuarioView(USERMODIFY_VIEW).getView());
        addViewFactory(NEWUSER_VIEW, () -> new Nuevo_usuarioView(NEWUSER_VIEW).getView());
        addViewFactory(MESSAGE_VIEW, () -> new MessageView(MESSAGE_VIEW).getView());
        addViewFactory(TICKETDETAIL_VIEW, () -> new TicketDetailView(TICKETDETAIL_VIEW).getView());
        addViewFactory(NEWTICKET_VIEW, () -> new NewTicketView(NEWTICKET_VIEW).getView());
        addViewFactory(PASSMODIFY_VIEW, () -> new PassModifyView(PASSMODIFY_VIEW).getView());
        addViewFactory(TICKET_VIEW, () -> new TicketView(TICKET_VIEW).getView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(TartangaTickets.class.getResource("style.css").toExternalForm());

    }
}
