package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;
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

    public ClientController(String serverString, int portInt) {

        this.serverAddress = serverString;
        this.serverPort = portInt;

        listen = new Listener();
        listen.start();

        this.view = new MainPanel(this);
        connect();

    }

    public void connect() {

        try {

            socket = new Socket(serverAddress, serverPort);

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            input = new ObjectInputStream(
                    socket.getInputStream());

            clientConnected = true;


        } catch (Exception e) {

            e.printStackTrace();

        } 

    }

    public void sendMessage(String message) {

        try {
            out.writeObject(new TrafficPackage(PackageType.MESSAGE, new Date(), new Message(message)));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            clientConnected = false;
            out.writeObject(new TrafficPackage(PackageType.DISCONNECT, new Date(), new Message("Disconnecting " + socket.getInetAddress())));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Listener extends Thread {

        @Override
        public void run() {
            
            while (clientConnected) {
                
                try {

                    System.out.println(input.readObject());

                } catch (Exception e) {
                    e.printStackTrace();
                    
                }

            }

        }

    }

}
