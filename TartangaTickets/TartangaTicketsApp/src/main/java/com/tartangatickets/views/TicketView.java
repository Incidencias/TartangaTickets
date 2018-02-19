package com.tartangatickets.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author Iker
 */

public class TicketView {

    private final String name;

    public TicketView(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            View view = FXMLLoader.load(TicketView.class.getResource("ver_incidencias.fxml"));
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
