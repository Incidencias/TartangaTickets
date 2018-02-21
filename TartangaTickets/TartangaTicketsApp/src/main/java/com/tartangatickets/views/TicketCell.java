/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.views;

import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.ListTile;
import com.tartangatickets.entities.Technician;
import com.tartangatickets.entities.Ticket;
import com.tartangatickets.entities.State.STATE;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author jon
 */
public class TicketCell extends CharmListCell<Ticket> {
    private final ListTile tile;
    private final ImageView image;
    
    public TicketCell() {
        this.tile = new ListTile();
        this.image = new ImageView();
                
        setText(null);
    }

    @Override
    public void updateItem(Ticket item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            tile.textProperty().setAll(
                    item.getTitle(), 
                    "Usuario: "+item.getUser().getFullName(), 
                    item.getTechnician()!=null?"Técnico: "+item.getTechnician().getFullName():"");
            String option = item.getState().name();
            String path = null;
            switch (option) {
                case "OPEN":
                    path = "file:src/main/resources/com/tartangatickets/drawable/open.png";
                    break;
                case "INPROGRESS":
                    path = "file:src/main/resources/com/tartangatickets/drawable/inprogress.png";  
                    break;
                case "BLOQUED":
                    path = "file:src/main/resources/com/tartangatickets/drawable/warning.png";
                    break;
                case "CLOSED":
                    path = "file:src/main/resources/com/tartangatickets/drawable/done.png";
                    break;
            }
            
            Image state = new Image(path, 30, 0, true, true);
            if (image != null) {
                image.setImage(state);
                tile.setSecondaryGraphic(image);
            }
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
