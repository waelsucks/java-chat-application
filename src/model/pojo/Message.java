package model.pojo;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Message implements PackageInterface, Serializable {
    
    private String message;
    private ImageIcon image;

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public Message(String message, ImageIcon image) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
