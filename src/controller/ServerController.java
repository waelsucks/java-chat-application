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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;

import model.ClientHandler;
import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;
import view.ServerGUI;

/**
 * Class handles communication between server and ClientHandler.
 * Class handles storage of traffic and events.
 */
public class ServerController {

    private ServerSocket socket;
    private PropertyChangeSupport pcs;

    private HashMap<String, User> users;
    private ServerGUI view;
    private ArrayList<TrafficPackage> events;

    private HashMap<String, ArrayList<Message>> messageQueue;

    public ServerController(int port) {

        System.out.println("Starting server...");

        pcs = new PropertyChangeSupport(this);

        view = new ServerGUI(this);
        //events = new ArrayList<TrafficPackage>(); 
        messageQueue = new HashMap<String, ArrayList<Message>>();

        users = readUsers();
        events = readEvents();

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

    /**
     * Reads the events from the file and saves them in an ArrayList for easier access.
     */
    private ArrayList<TrafficPackage> readEvents() {

        events = null;

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("files/Events.chat")))) {

            events = (ArrayList<TrafficPackage>) ois.readObject();

        } catch (Exception e) {
            events = new ArrayList<TrafficPackage>();
            System.out.println("Resetting events...");
        }

        return events;
    }

    /**
     * Reads the users from the file and saves them in a HashMap for easier access.
     */
    private HashMap<String, User> readUsers() {

        users = null;

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("files/Users.chat")))) {

            users = (HashMap<String, User>) ois.readObject();

        } catch (Exception e) {
            users = new HashMap<String, User>();
            System.out.println("Resetting users...");
        }

        return users;
    }

    /**
     * Creates and adds a new user to the HashMap.
     * @param username
     * @param name
     * @param image
     */
    public void createUser(String username, String name, ImageIcon image) {

        synchronized (users) {

            users.put(username, new User(name, UserGroup.USER, username, image));
            updateUsers();

        }

    }

    /**
     * Getter.
     * @param username
     * @return
     */
    public User getUser(String username) {

        User user = null;

        user = users.get(username);

        return user;
    }

    /**
     * Writes the current users hashmap to the Users.chat file
     */
    public void updateUsers() {
        try (ObjectOutputStream ous = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Users.chat")))) {

            ous.writeObject(users);

            System.out.println("Made changes to users file...");

        } catch (Exception e) {
            e.printStackTrace();
        }

        readUsers();

    }

    /**
     * Writes the current users hashmap to the Users.chat file
     */
    public void updateEvents() {
        try (ObjectOutputStream ous = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Events.chat")))) {

            ous.writeObject(events);

            System.out.println("Made changes to events file...");

        } catch (Exception e) {
            e.printStackTrace();
        }

        readEvents();

    }

    /**
     * Adds the event to the file, checks what type of event it is, 
     * and prints it in the Traffic GUI accordingly.
     * @param tp
     */
    public void logEvent(TrafficPackage tp) {

        events.add(tp);

        updateEvents();

        StringBuilder string = new StringBuilder();

        if (tp.getType() == PackageType.MESSAGE) {

            Message message = (Message) tp.getEvent();

            string.append(String.format("[%s]\n", new Date()));
            string.append(tp.getUser().getName() + " has sent a message to:\n");

            for (String id : message.getRecieverID()) {
                string.append(">> " + getUser(id).getName() + "\n");
            }
        }

        if (tp.getType() == PackageType.SIGN_UP) {

            string.append(String.format("[%s]\n", new Date()));
            string.append(tp.getUser().getName() + " registered!\n");

            sendMessage(
                    new TrafficPackage(PackageType.MESSAGE, new Date(), new Message("Registered!", null),
                            tp.getUser()));

        }

        if (tp.getType() == PackageType.CLIENT_CONNECT) {

            string.append(String.format("[%s]\n", new Date()));
            string.append(tp.getUser().getName() + " connected!\n");

            sendMessage(
                    new TrafficPackage(PackageType.MESSAGE, new Date(), new Message("Connected!", null), tp.getUser()));

        }

        if (tp.getType() == PackageType.CLIENT_DISCONNECT) {

            string.append(String.format("[%s]\n", new Date()));
            string.append(tp.getUser().getName() + " disconnected!\n");

            sendMessage(
                    new TrafficPackage(PackageType.MESSAGE, new Date(), new Message("Disconnected!", null),
                            tp.getUser()));

        }

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

    /**
     * 
     * @param tp
     */
    public void broadcast(TrafficPackage tp) {

        pcs.firePropertyChange("message", null, tp);
        logEvent(tp);

    }


    public void sendMessage(TrafficPackage tp) {

        Message message = (Message) tp.getEvent();

        if (message.getRecieverID().isEmpty()) {

            // Add all online users as recievers in case recievers is empty. ( public message
            // )

            for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {

                ClientHandler handler = (ClientHandler) pcl;

                message.getRecieverID().add(handler.getUsername());

            }
        }

        for (String username : message.getRecieverID()) {

            // Here we queue messages for a user's return

            boolean isUserOnline = Arrays.asList(pcs.getPropertyChangeListeners()).stream()
                    .anyMatch(o -> ((ClientHandler) o).getUsername().equals(username));

            if (!isUserOnline) {

                if (messageQueue.get(username) == null) {
                    messageQueue.put(username, new ArrayList<Message>());
                }

                messageQueue.get(username).add(message);

            }

        }

        for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {

            ClientHandler handler = (ClientHandler) pcl;

            if (message.getRecieverID().contains(handler.getUsername())) {
                pcl.propertyChange(new PropertyChangeEvent(this, "message", null, tp));
            }

        }

        logEvent(tp);

    }

    public HashMap<String, ArrayList<Message>> getMessageQueue() {
        return this.messageQueue;
    }

    public void setMessageQueue(HashMap<String, ArrayList<Message>> messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Gets events from the file between an interval.
     * @param from
     * @param to
     * @return
     */
    public ArrayList<TrafficPackage> getEventsBetween(Date from, Date to) {

        ArrayList<TrafficPackage> sorted = new ArrayList<TrafficPackage>();

        for (TrafficPackage event : events) {

            if ((event.getDate().compareTo(from) >= 0) && (event.getDate().compareTo(to) <= 0)) {
                sorted.add(event);
            }

        }

        return sorted;

    }

}
