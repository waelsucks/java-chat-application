package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.pojo.Message;
import model.pojo.PackageInterface;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserList;
import view.ClientGUI;
import view.UserGUI;

/**
 * Controllern för varje klient.
 */
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

    /**
     * Initializes the packet-transfers and handles incoming traffic.
     */
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

                            setUser((User) tp.getEvent());

                            JOptionPane.showMessageDialog(null, "Welcome " + user.getName());

                            outputStream
                                    .writeObject(new TrafficPackage(PackageType.USER_ONLINE, new Date(), null, getUser()));

                            view.setContactBoxValue(getUser().getFriends());

                            break;

                        case GET_ONLINE_USERS:

                            view.setUserBoxValue((UserList) tp.getEvent());

                            usersOnline = (UserList) tp.getEvent();

                            break;

                        case GET_USER:

                            openProfile((User) tp.getEvent());

                            break;

                        case MESSAGE:

                            Message message = (Message) tp.getEvent();

                            StyledDocument document = (StyledDocument) view.getChatBox().getDocument();
                            Style m = document.addStyle("message", null);

                            document.insertString(document.getLength(),
                                    String.format("[%s] >> %s\n", tp.getUser().getName(), message.getMessage()),
                                    null);
                            
                            if (message.getImage() != null) {
                                
                                StyleConstants.setIcon(m, message.getImage());
                                document.insertString(document.getLength(), "\n", m);                                

                            }
                            
                            view.getChatBox().setDocument(document);

                            break;

                        case ADD_CONTACT:

                            setUser((User) tp.getEvent());

                            view.setContactBoxValue(getUser().getFriends());

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

    /**
     * Connects to the streams and checks the online users.
     */
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

    /**
     * Shows the profile for selected user.
     * @param toShow
     */
    public void openProfile(User toShow) {

        UserGUI profile = new UserGUI(this);

        profile.setName(toShow.getName());
        profile.setProfilePic(toShow.getIconFile());
        profile.setUsername(toShow.getUserID());

        if (toShow.getStatus()) {
            profile.setStatus("Online");
        } else {
            profile.setStatus("Offline");
        }

    }

    /**
     * Generates a traffic package with the given event. Event must implement
     * PackageInterface
     * @return A traffic package generated from the event.
     */

    private TrafficPackage createPackage(PackageType type, PackageInterface event) {

        TrafficPackage packageToSend = new TrafficPackage(type, new Date(), event, user);

        return packageToSend;

    }

    /**
     * Sends a message with or without an image to the selected users.
     * @param text
     * @param icon
     * @param recievers
     */

    public void sendMessage(String text, ImageIcon icon , ArrayList<String> recievers) {

        // If it is a public message...

        Message message = new Message(text, null);

        message.setTimeSent(new Date());
        message.setSenderID(user.getUserID());
        message.setImage(icon);

        if (recievers == null || recievers.size() == 0) {

            for (User userOnline : usersOnline) {
                message.getRecieverID().add(userOnline.getUserID());
            }

        } else {

            for (String string : recievers) {
                message.getRecieverID().add(string);
            }

            message.getRecieverID().add(getUser().getUserID());

        }

        try {
            outputStream
                    .writeObject(new TrafficPackage(PackageType.MESSAGE, new Date(), message, user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shuts the client if the button "Disconnect" is pressed (ClientGUI)
     */
    public void disconnect() {
        System.exit(0);
    }

    /**
     * Creates a new event sent to the stream.
     * @param text
     */
    public void addFriend(String text) {

        try {
            outputStream.writeObject(new TrafficPackage(PackageType.ADD_CONTACT, null, new Message(text, null), user));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a new event sent to the stream.
     * @param userID
     */
    public void getProfile(String userID) {

        try {
            outputStream.writeObject(new TrafficPackage(PackageType.GET_USER, null, new Message(userID, null), user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ObjectInputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientGUI getView() {
        return this.view;
    }

    public void setView(ClientGUI view) {
        this.view = view;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserList getUsersOnline() {
        return this.usersOnline;
    }

    public void setUsersOnline(UserList usersOnline) {
        this.usersOnline = usersOnline;
    }

}
