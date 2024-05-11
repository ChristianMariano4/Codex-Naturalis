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
            Thread clientThread = new Thread(client);
            clientThread.start();
            clientThread.join();
            //System.exit(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
