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

        // Handler recieves a TrafficPackage from a client
        // and report it to the server. Server will register this traffic package and
        // then tell the handler(s) what to do.

        TrafficPackage packageFromHandler = (TrafficPackage) evt.getNewValue();

        switch (packageFromHandler.getType()) {

            case CLIENT_CONNECT:

                // Client is connecting

                try {
                    pcs.firePropertyChange("package", null, packageFromHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CLIENT_DISCONNECT:

                // Client is disconnecting

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

    }

}
