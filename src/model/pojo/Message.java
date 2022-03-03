package model.pojo;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Message implements PackageInterface, Serializable {
    
    private String message;
    private ImageIcon icon;

    public Message(String message) {
        this.message = message;
    }

    public Message(ImageIcon icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public ImageIcon getIconMessage() {
        return this.icon;
    }
   
    public void setIcon() {
        this.icon = icon;
    }



}
