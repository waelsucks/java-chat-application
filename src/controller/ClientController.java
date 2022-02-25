package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import view.MainPanel;

public class ClientController {

    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;
    private Socket socket;
    private String serverAddress;
    private int serverPort;
    private MainPanel view;

    public ClientController(String serverString, int portInt) {

        this.serverAddress = serverString;
        this.serverPort = portInt;
        this.view = new MainPanel(this);

        Scanner in = new Scanner(System.in);

        try {

            socket = new Socket(serverAddress, serverPort);

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            input = new ObjectInputStream(
                    socket.getInputStream());

            Listener listen = new Listener(input, out);

            while (true) {

                // Main loop

                out.writeObject(in.nextLine());

            }

        } catch (Exception e) {

            System.out.println("Server shut down.");
            System.exit(0);

        } finally {

            try {

                socket.close();
                out.close();
                input.close();
                in.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public class Listener extends Thread {

        public Listener(ObjectInputStream input, ObjectOutputStream out) {

            start();

        }

        @Override
        public void run() {
            
            while (!interrupted()) {
                
                try {

                    System.out.println(input.readObject());

                } catch (Exception e) {
                    System.out.println("Server shut down.");
                    System.exit(0);
                }

            }

        }

    }

}
