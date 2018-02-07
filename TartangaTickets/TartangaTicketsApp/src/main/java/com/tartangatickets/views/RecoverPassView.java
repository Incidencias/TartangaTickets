package com.tartangatickets.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author 2dam
 */
public class RecoverPassView {
    private final String name;

    public RecoverPassView(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            View view = FXMLLoader.load(RecoverPassView.class.getResource("recuperar_pass.fxml"));
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
