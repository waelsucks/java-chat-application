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
import java.util.Random;

public class ClientHandler extends Thread implements PropertyChangeListener {

    private PropertyChangeSupport serverPcs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static ArrayList<String> usedID = new ArrayList<String>();

    public ClientHandler(Socket clientSocket, PropertyChangeSupport pcs) {

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

                Object message = in.readObject();
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

    private String generateUserID() {

        Random rand = new Random();
        String randomID =  String.valueOf(rand.nextInt(9999));

        while (usedID.contains(randomID)) {
            randomID = String.valueOf(rand.nextInt(9999));
        }

        return randomID;
    
    }

}
