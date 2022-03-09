package model.pojo;

import java.util.ArrayList;

/**
 * A "middle step" between ClientController and ClientGUI to 
 * show a list of users.
 */
public class UserList extends ArrayList<User> implements PackageInterface {

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
