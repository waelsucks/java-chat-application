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

import model.pojo.PackageInterface;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
import model.pojo.User;
import model.pojo.UserGroup;

public class ClientHandler extends Thread implements PropertyChangeListener {

    private PropertyChangeSupport serverPcs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket, PropertyChangeSupport pcs) {

        this.clientSocket = clientSocket;
        this.serverPcs = pcs;

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            

            // Check username

            String username = (String) in.readObject();

            User user = checkUser(username);
            out.writeObject(new TrafficPackage(PackageType.USER, new Date(), user));
            out.flush();

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
            if (user_.getUserID().equals(username)) {
                return user_;
            }
        }

        // write

        String name = null;
        
        try {
            out.writeObject(new TrafficPackage(PackageType.NEW_USER, new Date(), null));
            out.flush();
            name = (String) in.readObject();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Users.dat")))) {

            user = new User(name, UserGroup.USER, username, null);
            oos.writeObject(user);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                TrafficPackage message = (TrafficPackage) in.readObject();

                switch (message.getType()) {

                    case DISCONNECT:

                        clientSocket.close();
                        serverPcs.firePropertyChange("package", null, message);
                        interrupt();
                        break;

                    case MESSAGE:

                        serverPcs.firePropertyChange("package", null, message);
                        break;

                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    // private User createUser() {

    // // LoginGUI beep = new LoginGUI()

    // User user = new User(beep.getName(), beep.getGroup(), beep.getUserID(),
    // beep.getImage());

    // }

    // private String generateUserID(){

    // Random rand = new Random();
    // String randomID = String.valueOf(rand.nextInt(9999));

    // while(userID.contains(randomID)){
    // randomID = String
    // }

    // }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        TrafficPackage tp = (TrafficPackage) evt.getNewValue();

        switch (evt.getPropertyName()) {

            case "public message":

                switch (tp.getType()) {

                    case MESSAGE:
                        try {
                            out.writeObject(tp);
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

    public PropertyChangeSupport getServerPcs() {
        return this.serverPcs;
    }

    public void setServerPcs(PropertyChangeSupport serverPcs) {
        this.serverPcs = serverPcs;
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
