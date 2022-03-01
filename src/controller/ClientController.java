package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserList;
import view.MainPanel;

public class ClientController {

    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;
    private Socket socket;
    private String serverAddress;
    private int serverPort;
    private MainPanel view;
    private boolean clientConnected;
    private Listener listen;
    private User user;

    public ClientController(String serverString, int portInt) {

        this.serverAddress = serverString;
        this.serverPort = portInt;

        this.view = new MainPanel(this);

        connect();

    }

    public void connect() {

        // Logging in

        String username = JOptionPane.showInputDialog("Enter username");

        TrafficPackage usernamePackage = new TrafficPackage(PackageType.CLIENT_CONNECT, new Date(), new Message(username),
                null);

        try {

            socket = new Socket(serverAddress, serverPort);

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            input = new ObjectInputStream(
                    socket.getInputStream());

            out.writeObject(usernamePackage);

            clientConnected = true;
            listen = new Listener();
            listen.start();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void sendMessage(String message) {

        try {
            view.getMessageBox().setText(null);
            out.writeObject(new TrafficPackage(PackageType.MESSAGE, new Date(), new Message(message), user));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {

            out.writeObject(new TrafficPackage(PackageType.CLIENT_DISCONNECT, new Date(),
                    user, user));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addContact() {
        try {

            //TrafficPackage usernamePackage = new TrafficPackage(PackageType.ADD_CONTACT, new Date(), new Message(username),
            //    null);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Listener extends Thread {

        @Override
        public void run() {

            while (!interrupted()) {
                while (clientConnected) {

                    try {

                        // Client will listen for messages from the handler and act accordingly

                        TrafficPackage tp = (TrafficPackage) input.readObject();

                        switch (tp.getType()) {

                            case NEW_USER:

                                String name = JOptionPane.showInputDialog("Welcome! Enter your name: ");

                                TrafficPackage namePackage = new TrafficPackage(PackageType.MESSAGE, new Date(), new Message(name), null);

                                out.writeObject(namePackage);

                                break;

                            case USER:

                                user = (User) tp.getEvent();
                                break;

                            case MESSAGE:

                                String toWrite = String.format("[%s] >> %s \n", tp.getUser().getName(),
                                        tp.getEvent().getMessage());
                                view.getChatBox().append(toWrite);

                                updateOnlineUsers();

                                break;

                            case CLIENT_DISCONNECT:

                                interrupt();

                                break;

                            case ADD_CONTACT:

                                break;

                            default:
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        private void updateOnlineUsers() {

            // here we update the online users.

            try {

                out.writeObject(new TrafficPackage(PackageType.GET_ONLINE_USERS, new Date(), null, null));
                TrafficPackage tp = (TrafficPackage) input.readObject();

                view.setUserBoxValue((UserList) tp.getEvent());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
