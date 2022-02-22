package main;

import controller.ClientController;
import controller.ServerController;

public class App {
    public static void main(String[] args) {
        
        switch (args[0]) {
            case "server":
                new ServerController();
                break;

            default:
                new ClientController();
        }

    }
}
