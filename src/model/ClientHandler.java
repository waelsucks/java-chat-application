package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.jar.Attributes.Name;

import controller.ServerController;
import model.pojo.PackageInterface;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;

public class ClientHandler extends Thread implements PropertyChangeListener {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ClientHandler(Socket clientSocket, ServerController serverController) {

        this.clientSocket = clientSocket;
        addPropertyChangeListener(serverController);

        try {

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        start();

    }

    private User checkUser(String username) {

        // read

        User user = null;

        ArrayList<User> persons = new ArrayList<User>();

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("files/Users.dat")))) {

            try {
                user = (User) ois.readObject();

                while (user != null && user instanceof User) {
                    persons.add(user);
                    
                    try {
                        user = (User) ois.readObject();
                    } catch (Exception e) {
                        System.out.println("End of file!");
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (User user_ : persons) {
            System.out.println(user_.getName());
            if (user_.getUserID().equals(username)) {
                return user_;
            }
        }

        // write

        String name = null;
        
        try {
            out.writeObject(new TrafficPackage(PackageType.NEW_USER, new Date(), null, user));
            out.flush();
            name = (String) in.readObject();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Users.dat")))) {

            user = new User(name, UserGroup.USER, username, null);
            out.writeObject(new TrafficPackage(PackageType.USER, new Date(), user, user));
            out.flush();
            oos.writeObject(user);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                // The client handler awaits communication from the client and sends it to the server.

                TrafficPackage message = (TrafficPackage) in.readObject();

                switch (message.getType()) {

                    case CONNECT:

                        User user = checkUser(message.getEvent().getMessage());

                        TrafficPackage connectPackage = new TrafficPackage(PackageType.CONNECT, new Date(), user, user);

                        out.writeObject(connectPackage);
                        pcs.firePropertyChange("package", null, connectPackage);

                        break;

                    case DISCONNECT:

                        clientSocket.close();
                        pcs.firePropertyChange("package", null, message);
                        interrupt();
                        break;

                    case MESSAGE:

                        pcs.firePropertyChange("package", null, message);
                        break;

                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        TrafficPackage tpFromServer = (TrafficPackage) evt.getNewValue();

        switch (evt.getPropertyName()) {

            case "public message":

                switch (tpFromServer.getType()) {

                    case MESSAGE:
                        try {
                            out.writeObject(tpFromServer);
                            out.flush();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public PropertyChangeSupport getPcs() {
        return this.pcs;
    }

    public void setPcs(PropertyChangeSupport serverPcs) {
        this.pcs = serverPcs;
    }

    public ObjectInputStream getIn() {
        return this.in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return this.out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

}
