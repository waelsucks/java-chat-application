package model;

<<<<<<< HEAD
import java.beans.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import controller.*;
import model.pojo.*;
=======
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import controller.ServerController;
import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;
import model.pojo.UserList;
>>>>>>> bc187015a5d848dc9a0ee4128c76f818cb55b65b

public class ClientHandler extends Thread implements PropertyChangeListener {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ServerController controller;
    private User user;

    public ClientHandler(Socket clientSocket, ServerController serverController) {

        this.controller = serverController;
        this.clientSocket = clientSocket;

        addPropertyChangeListener(serverController);

        try {

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        start();

    }

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                // The clienthandler awaits communication from the client and sends it to the
                // server.

                TrafficPackage packageFromClient = (TrafficPackage) in.readObject();
                TrafficPackage packageToServer = null;

                // user = packageFromClient.getUser();

                switch (packageFromClient.getType()) {

                    case CLIENT_CONNECT:

                        // A client wants to connect. Handler will check if user exists, create a new
                        // one otherwise.

                        String username = packageFromClient.getEvent().getMessage();

                        user = getUser(username);

                        if (user == null) {
                            user = addUser(username);
                        }

                        TrafficPackage packageToClient = new TrafficPackage(PackageType.USER, new Date(), user, user);

                        out.writeObject(packageToClient);

                        // Report to server. ( change to normal function? )

                        pcs.firePropertyChange("package", null,
                                new TrafficPackage(PackageType.CLIENT_CONNECT, new Date(), user, user));

                        break;

                    case CLIENT_DISCONNECT:

                        packageToServer = new TrafficPackage(PackageType.CLIENT_DISCONNECT, new Date(),
                                user, user);

                        pcs.firePropertyChange("package", null, packageToServer);

                        break;

                    case MESSAGE:

                        packageToServer = new TrafficPackage(PackageType.MESSAGE, new Date(),
                                packageFromClient.getEvent(), user);
                        pcs.firePropertyChange("package", null, packageToServer);

                        break;

                    case GET_USER:

                        User userRequested = getUser(packageFromClient.getEvent().getMessage());

                        packageToClient = new TrafficPackage(PackageType.GET_USER, new Date(), userRequested, user);

                        try {
                            out.writeObject(packageToClient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;

                    case ADD_CONTACT:

                        String friend = packageFromClient.getEvent().getMessage();

                        getUser(user.getUserID()).addFriend(friend);
                        packageToClient = new TrafficPackage(PackageType.MESSAGE, new Date(),
                                new Message("*** Added contact ***", null), user);

                        break;

                    default:
                        break;
                }

            } catch (Exception e) {

                TrafficPackage packageToServer = new TrafficPackage(PackageType.CLIENT_DISCONNECT, new Date(),
                        user, user);

                pcs.firePropertyChange("package", null, packageToServer);

                interrupt();

            }

        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // Handler recieving packages from the server

        TrafficPackage packageFromServer = (TrafficPackage) evt.getNewValue();
        TrafficPackage packageToClient = null;

        switch (packageFromServer.getType()) {

            case CLIENT_CONNECT:

                packageToClient = new TrafficPackage(PackageType.MESSAGE, new Date(),
                        new Message("Connected!", null), packageFromServer.getUser());

                try {
                    out.writeObject(packageToClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case CLIENT_DISCONNECT:

                packageToClient = new TrafficPackage(PackageType.MESSAGE, new Date(), new Message("Disconnected!", null),
                        packageFromServer.getUser());

                try {
                    out.writeObject(packageToClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case MESSAGE:

                try {
                    out.writeObject(packageFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case GET_ONLINE_USERS:

                try {
                    out.writeObject(
                            new TrafficPackage(PackageType.GET_ONLINE_USERS, new Date(), getOnlineUsers(), null));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }

    }

    private User getUser(String username) {

        User userReturn = null;

        try {
            userReturn = controller.getUsers().get(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userReturn;
    }

    private User addUser(String username) {

        // Creates a user and adds them to the "database"

        try {
            out.writeObject(
                    new TrafficPackage(PackageType.NEW_USER, new Date(), new Message("Requesting username", null), null));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        TrafficPackage packageFromClient = null;

        try {
            packageFromClient = (TrafficPackage) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String name = packageFromClient.getEvent().getMessage();
        ////inser picture here?
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose your profile picture! (png/jpg)");
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ImageIcon image = new ImageIcon(file.getAbsolutePath());
            controller.getUsers().put(name, new User(name, UserGroup.USER, username, image));
        }

        synchronized (controller.getUsers()) {
            controller.getUsers().notifyAll();
        }

        return getUser(username);
    }

    private UserList getOnlineUsers() {

        UserList onlineUsers = new UserList();

        synchronized (controller.getUsers()) {
            for (User user : controller.getUsers().values()) {
                if (user.getStatus()) {
                    onlineUsers.add(user);
                }
            }
        }

        return onlineUsers;

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public PropertyChangeSupport getPcs() {
        return this.pcs;
    }

    public void setPcs(PropertyChangeSupport serverPcs) {
        this.pcs = serverPcs;
    }

    public ObjectInputStream getIn() {
        return this.in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return this.out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

}
