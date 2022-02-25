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

public class ServerController extends Thread implements PropertyChangeListener{

    private ServerSocket serverSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ServerGUI serverGUI;

    private ArrayList<TrafficPackage> events;

    public ServerController(int port) {

        System.out.println("Starting server!");

        // // TESTING

        // try (ObjectOutputStream oos = new ObjectOutputStream(
        //         new BufferedOutputStream(new FileOutputStream("files/Users.dat")))) {

        //     User user = new User("NEW GUY", UserGroup.USER, "A NEW BEGINNING", null);
        //     oos.writeObject(user);
        //     oos.flush();
        //     oos.close();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // // -----------

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

                Socket clientSocket = serverSocket.accept();

                TrafficPackage tp = new TrafficPackage(PackageType.CONNECT, new Date(), new Message("A new client has connected from " + clientSocket.getInetAddress()));
                
                events.add(tp);
                serverGUI.getTrafficBox().append(String.format("[%s] >> %s \n", tp.getDate(), tp.getEvent().getMessage()));

                System.out.println("A user has joined. Referring them to a handler.");
                addPropertyChangeListener(new ClientHandler(clientSocket, pcs));


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

                TrafficPackage tp = (TrafficPackage) evt.getNewValue();

                switch (tp.getType()) {

                    case MESSAGE:
                        pcs.firePropertyChange("public message", null, tp);
                        break;
                
                    default:
                        break;
                }

                events.add(tp);
                serverGUI.getTrafficBox().append(String.format("[%s] >> %s \n", tp.getDate(), tp.getType()));
                
                break;
        
            default:
                break;
        }
        
    }

}
