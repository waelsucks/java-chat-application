package model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * A Class meant to be used by the Server to create and save a new User. A
 * client will
 * then enter a username and recieve the correct user object.
 */

public class User implements Serializable, PackageInterface {

    private String userID;
    private String name;
    private UserGroup group;
    private ImageIcon icon;

    public User(String name, UserGroup group, String userID, ImageIcon icon) {

        this.name = name;
        this.group = group;
        this.icon = icon;
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserGroup getGroup() {
        return this.group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public void setImage(ImageIcon icon){
        this.icon = icon;
    }

    public ImageIcon getImage(){
        return this.icon;
    }

    @Override
    public String getMessage() {
        return "I am a user";
    }

}
