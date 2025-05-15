package com.example.desktoppet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Notification {
    PetController petController;
    Image chatIcon = new Image(getClass().getResource("/NotificationIcons/MessageNotification.png").toExternalForm());;
    ImageView notificationIcon = new ImageView();
    double size = 20;

    public Notification(PetController petController) {
        this.petController = petController;
//        notificationIcon.setImage(chatIcon);
        notificationIcon.setFitHeight(20);
        notificationIcon.setFitWidth(20);
    }

    public void setPos(Double xPos, Double yPos){
        notificationIcon.setLayoutX(xPos);
        notificationIcon.setLayoutY(yPos);
    }

    public void setSize(Double size){
        this.size = size;
        notificationIcon.setFitWidth(size);
        notificationIcon.setFitHeight(size);
    }

    public double getSize(){
        return size;
    }

    public ImageView getNotification(){
        return notificationIcon;
    }

    public void setChatIcon(){
        notificationIcon.setImage(chatIcon);
    }

    public void setNoIcon(){
        notificationIcon.setImage(null);
    }
}
