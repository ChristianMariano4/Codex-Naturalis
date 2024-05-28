package it.polimi.ingsw.main;

import it.polimi.ingsw.network.server.Server;

import java.util.HashMap;
import java.util.Scanner;

public class MainServer {

    public static void main(String[] args) {
        System.out.println("Insert 1 to recover the last games, 0 otherwise: ");
        Scanner scanner = new Scanner(System.in);
        int recoverGames = scanner.nextInt();
        Server server = new Server(new HashMap<>(), recoverGames);
        server.start();
    }

}
