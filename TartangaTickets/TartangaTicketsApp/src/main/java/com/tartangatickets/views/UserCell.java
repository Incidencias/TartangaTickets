/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.ListTile;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.User;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Charm list with users
 *  @author Sergio López, Iker Jon Mediavilla, Ionut Savin, Jon Zaballa
 *  @version 1.0, Feb 21 2018
 */
public class UserCell extends CharmListCell<User> {
    private final ListTile tile;
    private final ImageView image;
    
    /**
     * Constructor 
     */
    public UserCell() {
        this.tile = new ListTile();
        this.image = new ImageView();
                
        setText(null);
    }

    /**
     * Set up the information of each user (name,department and login) and put an image if it's a technician
     * @param item
     * @param empty 
     */
    @Override
    public void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            tile.textProperty().setAll(
                    item.getFullName(), 
                    item.getDepartment().getName(), 
                    item.getLogin());
            if (item instanceof Technician) {
                
                Image wrench = new Image("file:src/main/resources/com/tartangatickets/drawable/wrench.png", 30, 0, true, true);
                if (image != null) {
                    image.setImage(wrench);
                    tile.setSecondaryGraphic(image);
                }
                
            }
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
