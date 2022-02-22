package controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends Thread {

    private ServerSocket serverSocket;
    private PropertyChangeSupport pcs;

    public ServerController(int port) {

        System.out.println("Starting server!");

        pcs = new PropertyChangeSupport(this);

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
                System.out.println("A client has connected!");

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

}
