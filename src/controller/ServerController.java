package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import model.ClientHandler;
import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;
import view.ServerGUI;

public class ServerController extends Thread implements PropertyChangeListener {

    private ServerSocket serverSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ServerGUI serverGUI;

    private ArrayList<TrafficPackage> events;

    public ServerController(int port) {

        System.out.println("Starting server!");

        pcs.addPropertyChangeListener(this);
        this.serverGUI = new ServerGUI(this);
        this.events = new ArrayList<TrafficPackage>();

        try {
            serverSocket = new ServerSocket(port);
            start();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {

            case "package":

                TrafficPackage tpFromHandler = (TrafficPackage) evt.getNewValue();

                switch (tpFromHandler.getType()) {

                    case MESSAGE:
                        pcs.firePropertyChange("public message", null, tpFromHandler);
                        break;

                    case CONNECT:

                        User user = tpFromHandler.getUser();
                        String message = tpFromHandler.getUser().getName() + " has logged in!";

                        TrafficPackage userConnect = new TrafficPackage(PackageType.MESSAGE, new Date(),
                                new Message(message), user);

                        pcs.firePropertyChange("public message", null, userConnect);
                        break;

                    default:
                        break;
                }

                events.add(tpFromHandler);
                serverGUI.getTrafficBox()
                        .append(String.format("[%s] >> %s \n", tpFromHandler.getDate(), tpFromHandler.getType()));

                break;

            default:
                break;
        }

    }

}
