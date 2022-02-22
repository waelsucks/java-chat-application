package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController extends Thread {

    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;
    private Socket socket;
    private String serverAddress;
    private int serverPort;

    public ClientController(String serverString, int portInt) {

        this.serverAddress = serverString;
        this.serverPort = portInt;

        start();

    }

    @Override
    public void run() {

        try {

            socket = new Socket(serverAddress, serverPort);

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            input = new ObjectInputStream(
                    socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
