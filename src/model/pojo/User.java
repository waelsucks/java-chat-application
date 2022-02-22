package model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * A Class meant to be used by the Server to create and save a new User. A client will 
 * then enter a username and recieve the correct user object.
 */

public class User implements Serializable {

    private String userID;
    private String name;
    private UserGroup group;
    private static ArrayList<String> usedID = new ArrayList<String>();

    public User(String name, UserGroup group) {

        this.name = name;
        this.group = group;

        this.userID = generateUserID();
        usedID.add(this.userID);

    }

    private String generateUserID() {

        Random rand = new Random();
        String randomID =  String.valueOf(rand.nextInt(9999));

        while (usedID.contains(randomID)) {
            randomID = String.valueOf(rand.nextInt(9999));
        }

        return randomID;
    
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

}
