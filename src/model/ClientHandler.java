package model;

import java.beans.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import controller.*;
import model.pojo.*;

public class ClientHandler extends Thread implements PropertyChangeListener {

    private Socket socket;
    private ServerController controller;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public ClientHandler(Socket clientSocket, ServerController controller) {

        this.socket = clientSocket;
        this.controller = controller;

        start();

    }

    @Override
    public void run() {

        try {

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // When user first joins, we ask for username.

            outputStream.writeObject(new TrafficPackage(PackageType.ASK_USERNAME, null, null, null));

            while (!interrupted()) {

                TrafficPackage tp = (TrafficPackage) inputStream.readObject();

                handleEvent(tp);

            }

        } catch (Exception e) {

            controller.logEvent(
                    new TrafficPackage(PackageType.CLIENT_DISCONNECT, new Date(), null, controller.getUser(username)));
            controller.userStatus(username, false);
            controller.getPcs().removePropertyChangeListener(this);

            // e.printStackTrace();
        }

    }

    private void handleEvent(TrafficPackage tp) throws IOException {

        // Handles events from client

        switch (tp.getType()) {

            case SIGN_UP:

                // A client sends credentials to sign up

                Message userSignup = (Message) tp.getEvent();
                controller.createUser(username, userSignup.getMessage(), userSignup.getImage());

                outputStream.writeObject(
                        new TrafficPackage(PackageType.USER, null, controller.getUser(username), null));

                controller.logEvent(
                        new TrafficPackage(PackageType.SIGN_UP, new Date(), null, controller.getUser(username)));

                break;

            case ASK_USERNAME:

                // A client sends username

                username = tp.getEvent().getMessage();

                if (controller.getUser(username) == null) {

                    outputStream.writeObject(
                            new TrafficPackage(PackageType.SIGN_UP, null, null, null));

                } else {

                    outputStream.writeObject(
                            new TrafficPackage(PackageType.USER, null, controller.getUser(username), null));

                    controller.logEvent(
                            new TrafficPackage(PackageType.CLIENT_CONNECT, new Date(), null,
                                    controller.getUser(username)));

                    if (controller.getMessageQueue().containsKey(username)) {
                        for (Message missedMessage : controller.getMessageQueue().get(username)) {
                            outputStream.writeObject(
                                    new TrafficPackage(PackageType.MESSAGE, new Date(), missedMessage,
                                            controller.getUser(
                                                    missedMessage.getSenderID())));
                        }

                        controller.getMessageQueue().get(username).clear();

                    }


                }
                break;

            case GET_ONLINE_USERS:

                UserList onlineUsers = new UserList();

                for (User user : controller.getUsers().values()) {
                    if (user.getStatus()) {
                        onlineUsers.add(user);
                    }
                }

                outputStream.writeObject(new TrafficPackage(PackageType.GET_ONLINE_USERS, null, onlineUsers, null));

                break;

            case USER_ONLINE:

                controller.userStatus(tp.getUser().getUserID(), true);

                break;

            case GET_USER:

                User toSend = controller.getUser(tp.getEvent().getMessage());
                // User toSend = new User("Tessa Testar", UserGroup.ADMIN, "beep", null);
                // toSend.setStatus(true);

                outputStream.writeObject(new TrafficPackage(PackageType.GET_USER, null, toSend, null));

                break;

            case MESSAGE:

                controller.sendMessage(tp);
                break;

            case ADD_CONTACT:

                if (!controller.getUser(username).getFriends().contains(tp.getEvent().getMessage())) {

                    controller.getUser(username).addFriend(tp.getEvent().getMessage());
                    controller.updateUsers();

                    User beep = controller.getUser(username);

                    outputStream.writeObject(new TrafficPackage(PackageType.ADD_CONTACT, null, beep, beep));

                }

                break;

            default:
                break;

        }

        outputStream.flush();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case "users":

                try {
                    handleEvent(new TrafficPackage(PackageType.GET_ONLINE_USERS, null, null, null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "message":

                try {
                    outputStream.writeObject(evt.getNewValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "user":

                break;

            default:
                break;
        }

    }

    public String getUsername() {
        return username;
    }

}
