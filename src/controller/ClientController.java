package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JOptionPane;
import model.pojo.*;
import model.pojo.TrafficPackage;
import model.pojo.User;
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

        connect();

        // Logging in

        String username = JOptionPane.showInputDialog("Enter username");

        try {
            out.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.view = new MainPanel(this);

    }

    public void connect() {

        try {

            socket = new Socket(serverAddress, serverPort);

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            input = new ObjectInputStream(
                    socket.getInputStream());

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
            clientConnected = false;
            out.writeObject(new TrafficPackage(PackageType.DISCONNECT, new Date(),
                    new Message("Disconnecting " + socket.getInetAddress()), user));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Listener extends Thread {

        @Override
        public void run() {

            while (!interrupted()) {
                while (clientConnected) {

                    try {

                        TrafficPackage tp = (TrafficPackage) input.readObject();

                        if (tp.getType() == PackageType.NEW_USER) {
                            String name = JOptionPane.showInputDialog("Welcome! Enter your name: ");
                            out.writeObject(name);
                        }

                        if (tp.getType() == PackageType.USER) {
                            user = (User) tp.getEvent();
                            System.out.println("My name is " + user.getName());
                        }

                        if (tp.getType() == PackageType.MESSAGE) {
                            String toWrite = String.format("[%s] >> %s \n", tp.getUser().getName(), tp.getEvent().getMessage());
                            view.getChatBox().append(toWrite);
                        }

                    } catch (Exception e) {
                        // System.out.println("Disconnecting");
                        // disconnect();
                    }

                }
            }

        }

    }

}
