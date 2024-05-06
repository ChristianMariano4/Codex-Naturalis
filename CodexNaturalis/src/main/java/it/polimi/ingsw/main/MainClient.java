package it.polimi.ingsw.main;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.ViewCLI;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.TUI;
import it.polimi.ingsw.view.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainClient {


    public static void main(String[] args) {
        ViewCLI view;
        System.out.println("Connecting to RMI server...");
        String serverName = "Server";

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
            ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);
            RMIClient client = new RMIClient(server);
            new Thread(client).start();
            view = new ViewCLI(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        view.run();
    }

//    public static void main(String[] args)
//    {
//        Scanner clientScanner = new Scanner(System.in);
//        System.out.println("Insert client ip (nothing for localhost): ");
//        String serverIp = clientScanner.nextLine();
//        if(serverIp.isEmpty())
//        {
//            serverIp = "localhost";
//        }
//        try{
//            Socket socket = new Socket(serverIp, GameValues.SERVER_PORT);
//            System.out.println("test1");
//
//            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//
//            while(true) {
//                System.out.println("test2");
//
//                //ObjectiveCard card = (ObjectiveCard) in.readObject();
//                Game game = (Game) in.readObject();
//                System.out.println("test");
//
//                System.out.println(game.getGameId());
//                System.out.println("testsdfs");
//            }
//
//        }
//        catch (Exception e)
//        {
//            System.err.println(e.getMessage());
//        }
//  }
}
