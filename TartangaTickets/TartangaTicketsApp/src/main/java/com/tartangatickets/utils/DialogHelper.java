/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import com.gluonhq.charm.glisten.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * 
 * 
 * Custom dialog with a given title and content
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018

 */
public final class DialogHelper extends Dialog{
    
    /**
     * Generate a dialog with personalized title and message
     * @param title String the title of the dialog
     * @param content String the message of the dialog
     */
    public static void newInstance(String title,String content){
        Dialog dialog = new Dialog();
        dialog.setTitle(new Label(title));
        dialog.setContent(new Label(content));
        Button okButton = new Button("Ok");
        okButton.setOnAction(e ->{
            dialog.hide();
        });
        dialog.getButtons().add(okButton);
        dialog.showAndWait();
        
    }
}
