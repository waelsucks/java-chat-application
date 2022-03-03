package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import model.ClientHandler;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserList;
import view.ServerGUI;

public class ServerController extends Thread implements PropertyChangeListener {

    private ServerSocket serverSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ServerGUI serverGUI;
    private HashMap<String, User> users = null;

    private ArrayList<TrafficPackage> events;

    public ServerController(int port) {

        System.out.println("Starting server!");

        users = readUsers();
        new usersListener();

        this.serverGUI = new ServerGUI(this);
        this.events = new ArrayList<TrafficPackage>();

        try {
            serverSocket = new ServerSocket(port);
            start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, User> readUsers() {

        ArrayList<User> persons = null;
        HashMap<String, User> users = null;

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("files/Users.chat")))) {

            users = (HashMap<String, User>) ois.readObject();

        } catch (Exception e) {
            users = new HashMap<String, User>();
            System.out.println("Resetting users...");
        }

        return users;
    }

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                // Waiting for a client to connect

                Socket clientSocket = serverSocket.accept();

                // Redirecting client to a handler and setting up a two way communication
                // between handler and server

                ClientHandler handler = new ClientHandler(clientSocket, this);
                addPropertyChangeListener(handler);

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    private class usersListener extends Thread {

        public usersListener() {

            start();

        }

        @Override
        public void run() {

            while (true) {
                try {

                    synchronized (users) {

                        users.wait();

                        try (ObjectOutputStream ous = new ObjectOutputStream(
                                new BufferedOutputStream(new FileOutputStream("files/Users.chat")))) {

                            ous.writeObject(users);

                            pcs.firePropertyChange("package", null , new TrafficPackage(PackageType.GET_ONLINE_USERS, new Date(), null, null));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // Handler recieves a TrafficPackage from a client
        // and report it to the server. Server will register this traffic package and
        // then tell the handler(s) what to do.

        TrafficPackage packageFromHandler = (TrafficPackage) evt.getNewValue();

        switch (packageFromHandler.getType()) {

            case CLIENT_CONNECT:

                // Client is connecting

                // for (User user : users) {
                // if (packageFromHandler.getUser().getUserID().equals(user.getUserID())) {
                // user.setStatus(true);
                // }
                // }

                users.get(packageFromHandler.getUser().getUserID()).setStatus(true);

                try {
                    pcs.firePropertyChange("package", null, packageFromHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CLIENT_DISCONNECT:

                // Client is disconnecting

                // for (User user : users) {
                //     if (packageFromHandler.getUser().getUserID().equals(user.getUserID())) {
                //         user.setStatus(false);
                //     }
                // }

                users.get(packageFromHandler.getUser().getUserID()).setStatus(false);

                pcs.firePropertyChange("package", null, packageFromHandler);

                break;

            case MESSAGE:

                pcs.firePropertyChange("package", null, packageFromHandler);
                break;

            default:
                break;
        }

        events.add(packageFromHandler);

        serverGUI.getTrafficBox()
                .append(String.format("[%s] >> %s \n", packageFromHandler.getDate(),
                        packageFromHandler.getType()));

        synchronized (users) {
            users.notifyAll();
        }

    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public PropertyChangeSupport getPcs() {
        return this.pcs;
    }

    public void setPcs(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public ServerGUI getServerGUI() {
        return this.serverGUI;
    }

    public void setServerGUI(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public ArrayList<TrafficPackage> getEvents() {
        return this.events;
    }

    public void setEvents(ArrayList<TrafficPackage> events) {
        this.events = events;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

}
