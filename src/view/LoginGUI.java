package view;

import javax.swing.*;
import controller.ClientController;
import model.pojo.User;
import model.pojo.UserGroup;
import java.awt.*;

public class LoginGUI{

    private ClientController controller;
    private User user;
    
    public LoginGUI(ClientController controller){

        this.controller = controller;
        
    }

    public String getUserName(){
        return user.getName();
    }
    public UserGroup getUserGroup(){
        return user.getGroup();
    }
    public String getUserID(){
        return user.getUserID();
    }
    public ImageIcon getUserIcon(){
        return user.getImage();
    }


    


}