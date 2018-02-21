package com.tartangatickets;

import com.tartangatickets.views.LoginView;
import com.tartangatickets.views.MainMenuView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import com.tartangatickets.logic.Logic;
import com.tartangatickets.logic.LogicInterface;
import com.tartangatickets.views.MessageView;
import com.tartangatickets.views.TicketDetailView;
import com.tartangatickets.views.TicketListView;
import com.tartangatickets.views.UsersListView;


public class TartangaTickets extends MobileApplication {

    public static final String LOGIN_VIEW = HOME_VIEW;
    public static final String RECOVERPASS_VIEW = "RecoverPassView";
    public static final String MAINMENU_VIEW = "MainMenuView";
    public static final String NEWUSER_VIEW = "NewUserView";
    public static final String MESSAGE_VIEW = "MessageView";
    public static final String TICKETDETAIL_VIEW = "TicketDetailView";
    public static final String NEWTICKET_VIEW = "NewTicketView";
    public static final String PASSMODIFY_VIEW = "PassModifyView";
    public static final String USER_LIST_VIEW = "UsersListView";
    public static final String TICKET_LIST_VIEW = "TicketListView";
    public static final LogicInterface LOGIC = new Logic();

    
    @Override
    public void init() {
        
        addViewFactory(LOGIN_VIEW, () -> new LoginView(LOGIN_VIEW).getView());
        addViewFactory(MAINMENU_VIEW, () -> new MainMenuView(MAINMENU_VIEW).getView());
        addViewFactory(MESSAGE_VIEW, () -> new MessageView(MESSAGE_VIEW).getView());
        addViewFactory(USER_LIST_VIEW, () -> new UsersListView(USER_LIST_VIEW).getView());
        addViewFactory(TICKET_LIST_VIEW, () -> new TicketListView(TICKET_LIST_VIEW).getView());
        addViewFactory(TICKETDETAIL_VIEW, () -> new TicketDetailView(TICKETDETAIL_VIEW).getView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.AMBER.assignTo(scene);

        scene.getStylesheets().add(TartangaTickets.class.getResource("style.css").toExternalForm());
    }
}
