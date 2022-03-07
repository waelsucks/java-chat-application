package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;

import model.ClientHandler;
import model.pojo.Message;
import model.pojo.PackageInterface;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;
import view.ServerGUI;

public class ServerController {

    private ServerSocket socket;
    private PropertyChangeSupport pcs;

    private HashMap<String, User> users;
    private ServerGUI view;
    private ArrayList<TrafficPackage> events;

    public ServerController(int port) {

        System.out.println("Starting server...");

        pcs = new PropertyChangeSupport(this);

        view = new ServerGUI(this);
        events = new ArrayList<TrafficPackage>(); // This will later be replaced by a file containing events (?)

        users = readUsers();

        try {

            socket = new ServerSocket(port);

            while (true) {

                Socket clientSocket = socket.accept();
                addPropertyChangeListener(new ClientHandler(clientSocket, this));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    private HashMap<String, User> readUsers() {

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

    public void createUser(String username, String name, ImageIcon image) {

        synchronized (users) {

            users.put(username, new User(name, UserGroup.USER, username, image));
            updateUsers();

        }

    }

    public User getUser(String username) {

        User user = null;

        readUsers();

        synchronized (users) {
            user = users.get(username);
        }

        return user;
    }

    public void updateUsers() {

        // Writes the current users hashmap to the Users.chat file

        try (ObjectOutputStream ous = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Users.chat")))) {

            ous.writeObject(users);

        } catch (Exception e) {
            e.printStackTrace();
        }

        readUsers();

    }

    public void logEvent(TrafficPackage tp) {

        events.add(tp);

        StringBuilder string = new StringBuilder();

        if (tp.getType() == PackageType.MESSAGE) {

            Message message = (Message) tp.getEvent();

            string.append(String.format("[%s]\n", new Date()));
            string.append(tp.getUser().getName() + " has sent a message to:\n");

            for (String id : message.getRecieverID()) {
                string.append(">> " + getUser(id).getName() + "\n");
            }

        }

        // view.getTrafficBox()
        //         .append(String.format("[%s] >> %s \n", tp.getDate(),
        //                 tp.getType()));

        view.getTrafficBox().append(string.toString() + "\n____________<3_____________\n");

    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public PropertyChangeSupport getPcs() {
        return this.pcs;
    }

    public void setPcs(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public ServerGUI getView() {
        return this.view;
    }

    public void setView(ServerGUI view) {
        this.view = view;
    }

    public ArrayList<TrafficPackage> getEvents() {
        return this.events;
    }

    public void setEvents(ArrayList<TrafficPackage> events) {
        this.events = events;
    }

    public void userStatus(String username, boolean status) {

        users.get(username).setStatus(status);
        updateUsers();

        pcs.firePropertyChange("users", null, readUsers());

    }

    public void broadcast(TrafficPackage tp) {

        pcs.firePropertyChange("message", null, tp);
        logEvent(tp);

    }

    public void sendMessage(TrafficPackage tp) {

        Message message = (Message) tp.getEvent();

        for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {

            ClientHandler handler = (ClientHandler) pcl;

            if (message.getRecieverID().contains(handler.getUsername())) {
                pcl.propertyChange(new PropertyChangeEvent(this, "message", null, tp));
            }

        }

        logEvent(tp);

    }

}
