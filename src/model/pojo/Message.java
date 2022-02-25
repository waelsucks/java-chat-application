package model.pojo;

public class Message implements PackageInterface {
    
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
