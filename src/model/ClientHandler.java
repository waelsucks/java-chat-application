package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import model.pojo.TrafficPackage;
import model.pojo.User;

public class ClientHandler extends Thread implements PropertyChangeListener {

    private PropertyChangeSupport serverPcs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static ArrayList<String> userID = new ArrayList<String>();
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket, PropertyChangeSupport pcs) {
        
        this.clientSocket = clientSocket;
        this.serverPcs = pcs;

        try {

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        start();

    }

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                TrafficPackage message = (TrafficPackage) in.readObject();

                switch (message.getType()) {
                    
                    case DISCONNECT:

                        clientSocket.close();
                        serverPcs.firePropertyChange("package", null, message);
                        interrupt();
                        break;

                    case MESSAGE:

                        serverPcs.firePropertyChange("package", null, message);
                
                    default:
                        break;
                }
                
                serverPcs.firePropertyChange("message", null, message);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        try {
            out.writeObject(evt.getNewValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // private static User createUser(){

    //     user = new User(name, group, userID, icon)

    //     userID.add(user);
    // }

    private String generateUserID() {

        Random rand = new Random();
        String randomID =  String.valueOf(rand.nextInt(9999));

        while (userID.contains(randomID)) {
            randomID = String.valueOf(rand.nextInt(9999));
        }

        return randomID;
    
    }

    

}
