package controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.ClientHandler;

public class ServerController extends Thread {

    private ServerSocket serverSocket;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);;

    public ServerController(int port) {

        System.out.println("Starting server!");

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

                addPropertyChangeListener(new ClientHandler(clientSocket, pcs));
                System.out.println("A user has joined. Referring them to a handler.");

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

}
