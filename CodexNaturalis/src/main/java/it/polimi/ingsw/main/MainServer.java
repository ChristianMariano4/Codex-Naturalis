package it.polimi.ingsw.main;

import it.polimi.ingsw.network.server.Server;

import java.util.HashMap;

public class MainServer {

    public static void main(String[] args) {
        Server server = new Server(new HashMap<>());
        server.start();
    }

}
