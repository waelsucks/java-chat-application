package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

import model.pojo.Message;
import model.pojo.PackageInterface;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserList;
import view.ClientGUI;
import view.UserGUI;

public class ClientController {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String address;
    private int port;

    private Socket socket;

    private ClientGUI view;
    private User user;

    private UserList usersOnline = null;

    public ClientController(String address, int port) {

        this.address = address;
        this.port = port;
        this.view = new ClientGUI(this);
        

        connect();

    }

    private class Listener extends Thread {

        public Listener() {

            start();

        }

        @Override
        public void run() {

            try {

                while (!interrupted()) {

                    // Awaits messages from the server.

                    TrafficPackage tp = (TrafficPackage) inputStream.readObject();

                    switch (tp.getType()) {

                        case ASK_USERNAME:

                            String username = JOptionPane.showInputDialog("Enter username");

                            outputStream.writeObject(
                                    createPackage(PackageType.ASK_USERNAME, new Message(username, null)));

                            break;

                        case SIGN_UP:

                            String name = JOptionPane.showInputDialog("Enter name");
                            ImageIcon image = view.chooseImage();

                            outputStream.writeObject(
                                    createPackage(PackageType.SIGN_UP, new Message(name, image)));

                            break;

                        case USER:

                            user = (User) tp.getEvent();

                            JOptionPane.showMessageDialog(null, "Welcome " + user.getName());

                            outputStream
                                    .writeObject(new TrafficPackage(PackageType.USER_ONLINE, new Date(), null, user));

                            break;

                        case GET_ONLINE_USERS:

                            view.setUserBoxValue((UserList) tp.getEvent());
                            usersOnline = (UserList)tp.getEvent();

                            break;

                        case GET_USER:

                            openProfile((User) tp.getEvent());

                            break;

                        case MESSAGE:

                            StyledDocument document = (StyledDocument) view.getChatBox().getDocument();
                            document.insertString(document.getLength(),
                                    String.format("[%s] >> %s\n", tp.getUser().getName(), tp.getEvent().getMessage()), null);
                            view.getChatBox().setDocument(document);

                            break;

                        default:

                            break;

                    }

                    outputStream.flush();

                }

            } catch (Exception e) {
                e.printStackTrace();
                interrupt();
            }

        }

    }

    public void connect() {

        user = null;

        try {

            socket = new Socket(address, port);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            new Listener();

            outputStream.writeObject(new TrafficPackage(PackageType.GET_ONLINE_USERS, null, null, null));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openProfile(User toShow) {

        UserGUI profile = new UserGUI(this);

        profile.setName(toShow.getName());
        profile.setProfilePic(toShow.getIconFile());
        profile.setUsername(toShow.getUserID());

        if (toShow.getStatus() || toShow.getUserID().equals(user.getUserID())) {
            profile.setStatus("Online");
        } else {
            profile.setStatus("Offline");
        }

    }

    /**
     * Generates a traffic package with the given event. Event must implement
     * PackageInterface
     * 
     * @return A traffic package generated from the event.
     */

    private TrafficPackage createPackage(PackageType type, PackageInterface event) {

        TrafficPackage packageToSend = new TrafficPackage(type, new Date(), event, user);

        return packageToSend;

    }

    public void sendMessage(String text) {

        // If it is a public message...

        Message message = new Message(text, null);

        message.setTimeSent(new Date());
        message.setSenderID(user.getUserID());
        
        boolean publicMessage = true;

        if (publicMessage) {
            
            for (User userOnline : usersOnline) {
                message.getRecieverID().add(userOnline.getUserID());
            }

        }

        try {
            outputStream
                    .writeObject(new TrafficPackage(PackageType.MESSAGE, new Date(), message, user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
    }

    public void getProfile(String userID) {

        try {
            outputStream.writeObject(new TrafficPackage(PackageType.GET_USER, null, new Message(userID, null), user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendPic(String text, ImageIcon icon) {
    }

    public void addFriend(String text) {
    }

}
