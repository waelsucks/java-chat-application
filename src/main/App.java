package main;

import controller.ClientController;
import controller.ServerController;

/**
 * This script starts the application. All connections will be using the port 2343. Use the following syntax:
 * <p>
 * To start a server on the localhost:
 * <p>
 * >> java App.java server
 * <p>
 * To start a client on the localhost:
 * <p>
 * >> java App.java client
 * <p>
 * To start a client on another server address:
 * <p>
 * >> java App.java client 999.999.999.999
 */
public class App {
    public static void main(String[] args) {

        int serverPort = 2343;
        String serverAddress = "127.0.0.1";

        switch (args[0]) {

            case "server":

                // Starts a server on port 2343.

                new ServerController(2343);
                break;

            case "client":

                // If a specific address is given

                if (args.length == 2) {
                    new ClientController(args[1], serverPort);
                } else {
                    new ClientController(serverAddress, serverPort);
                }

                break;

        }

    }
}
