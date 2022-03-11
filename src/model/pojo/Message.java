package model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * Class encapsulates data. 
 * Used for sending messages and pictures.
 */
public class Message implements PackageInterface, Serializable {

    private String message;
    private String senderID;
    private ArrayList<String> recieverID;
    private Date timeSent;
    private Date timeRecieved;
    private ImageIcon image;

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public Message(String message, ImageIcon image) {
        this.message = message;
        this.image = image;
        this.recieverID = new ArrayList<String>();
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return this.senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public ArrayList<String> getRecieverID() {
        return this.recieverID;
    }

    public void setRecieverID(ArrayList<String> recieverID) {
        this.recieverID = recieverID;
    }

    public Date getTimeSent() {
        return this.timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public Date getTimeRecieved() {
        return this.timeRecieved;
    }

    public void setTimeRecieved(Date timeRecieved) {
        this.timeRecieved = timeRecieved;
    }

}
