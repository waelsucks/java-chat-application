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

import controller.ServerController;
import model.pojo.Message;
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

    @Override
    public void run() {

        while (!interrupted()) {

            try {

                // The client handler awaits communication from the client and sends it to the
                // server.

                TrafficPackage packageFromClient = (TrafficPackage) in.readObject();
                TrafficPackage packageToServer = null;

                User user = packageFromClient.getUser();

                switch (packageFromClient.getType()) {

                    case CLIENT_CONNECT:

                        // A client wants to connect. Handler will check if user exists, create a new
                        // one otherwise.

                        String username = packageFromClient.getEvent().getMessage();

                        user = getUser(username);

                        if (user == null) {
                            user = createUser(username);
                        }

                        TrafficPackage packageToClient = new TrafficPackage(PackageType.USER, new Date(), user, user);

                        out.writeObject(packageToClient);

                        // Report to server. ( change to normal function? )

                        pcs.firePropertyChange("package", null,
                                new TrafficPackage(PackageType.CLIENT_CONNECT, new Date(), user, user));

                        break;

                    case CLIENT_DISCONNECT:

                        packageToServer = new TrafficPackage(PackageType.CLIENT_DISCONNECT, new Date(),
                                user, user);

                        pcs.firePropertyChange("package", null, packageToServer);
                        interrupt();

                        break;

                    case MESSAGE:

                        packageToServer = new TrafficPackage(PackageType.MESSAGE, new Date(),
                                packageFromClient.getEvent(), user);
                        pcs.firePropertyChange("package", null, packageToServer);

                        break;

                    default:
                        break;
                }

            } catch (Exception e) {
                interrupt();
            }

        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // Handler recieving packages from the server

        TrafficPackage packageFromServer = (TrafficPackage) evt.getNewValue();
        TrafficPackage packageToClient = null;

        switch (packageFromServer.getType()) {

            case CLIENT_CONNECT:

                packageToClient = new TrafficPackage(PackageType.MESSAGE, new Date(),
                        new Message("Connected!"), packageFromServer.getUser());

                try {
                    out.writeObject(packageToClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case CLIENT_DISCONNECT:

                packageToClient = new TrafficPackage(PackageType.MESSAGE, new Date(), new Message("Disconnected!"),
                        packageFromServer.getUser());

                try {
                    out.writeObject(packageToClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case MESSAGE:

                try {
                    out.writeObject(packageFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }

    }

    private User getUser(String username) {

        User user = null;

        ArrayList<User> persons = new ArrayList<User>();

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("files/Users.chat")))) {

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

        return null;
    }

    private User createUser(String username) {

        // Creates a user and adds them to the "database"

        User user = null;

        try {

            out.writeObject(new TrafficPackage(PackageType.NEW_USER, new Date(), null, null));
            out.flush();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        TrafficPackage packageFromClient = null;

        try {
            packageFromClient = (TrafficPackage) in.readObject();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String name = packageFromClient.getEvent().getMessage();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/Users.chat")))) {

            user = new User(name, UserGroup.USER, username, null);

            oos.writeObject(user);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

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
