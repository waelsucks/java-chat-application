package model.pojo;

import java.io.Serializable;

public class Message implements PackageInterface, Serializable {
    
    private String message;


    public Message(String message) {
        this.message = message;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
