package it.polimi.ingsw.main;

import it.polimi.ingsw.network.server.Server;

import java.util.HashMap;

/**
 * The main class of the server
 */
public class MainServer {
    /**
     * The main method of the class used to start the server
     * @param args not used
     */
    public static void main(String[] args) {
        Server server = new Server(new HashMap<>());
        server.start();
    }

}
