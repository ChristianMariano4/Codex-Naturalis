package it.polimi.ingsw.main;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.RMIClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainClient {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
        try {
            System.out.println("Choose how to connect: ");
            System.out.println("1 - Socket + TUI");
            System.out.println("2 - RMI + TUI");
            System.out.println();
            System.out.println("3 - Socket + GUI");
            System.out.println("4 - RMI + GUI");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice){
                case 1:
                    MainClient.SocketTUI();
                    break;
                case 2:
                    MainClient.RMITUI();
                    break;
                case 3:
                    MainClient.SocketGUI();
                    break;
                case 4:
                    MainClient.RMIGUI();
                    break;
                default:
                    throw new NumberFormatException();
            }
            break;
        }
        catch (ServerDisconnectedException e)
        {
            continue;
        }
        catch (Exception e)
        {
            System.out.println("Invalid input, try again");
        }
        }while(true);
        scanner.close();



    }
    private static void SocketTUI() throws ServerDisconnectedException {
        System.out.println("Insert server IP address, leave empty for localhost: ");
        Scanner scanner = new Scanner(System.in);
        String serverIP = scanner.nextLine();
        if(serverIP.isEmpty())
            serverIP = "localhost";
        SocketClient client = null;
        try {
            client = new SocketClient(serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        client.connectToServer();


//        try {
//            System.out.println("Insert server IP address, leave empty for localhost: ");
//            Scanner scanner = new Scanner(System.in);
//            String serverIP = scanner.nextLine();
//            if(serverIP.equals(""))
//                serverIP = "localhost";
//            Socket serverSocket = new Socket(serverIP, GameValues.SOCKET_SERVER_PORT);
//            System.out.println("Connected to sever successfully");
//            SocketClient client = new SocketClient(serverSocket);
//            Thread clientThread = new Thread(client);
//            clientThread.start();
//            clientThread.join();
//
//        }
//        catch(IOException e) {
//            System.err.println("Couldn't connect to server");
//            throw new ServerDisconnectedException();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return;
    }
    private static void RMITUI() throws ServerDisconnectedException {
        System.out.println("Insert server IP address, leave empty for localhost: ");
        Scanner scanner = new Scanner(System.in);
        String serverIP = scanner.nextLine();
        if(serverIP.isEmpty())
            serverIP = "localhost";
        System.out.println("Connecting to RMI server...");
        RMIClient client = null;
        try {
            client = new RMIClient(serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        client.connectToServer();


//        String serverName = "Server";
//
//        try {
//            System.out.println("Insert server IP address, leave empty for localhost: ");
//            Scanner scanner = new Scanner(System.in);
//            String serverIP = scanner.nextLine();
//            if(serverIP.equals(""))
//                serverIP = "localhost";
//            System.out.println("Connecting to RMI server...");
//            Registry registry = LocateRegistry.getRegistry(serverIP,  GameValues.RMI_SERVER_PORT);
//            ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);
//            RMIClient client = new RMIClient(server);
//            Thread clientThread = new Thread(client);
//            clientThread.start();
//            clientThread.join();
//            //System.exit(0);
//        } catch (RemoteException e) {
//            System.err.println("Couldn't connect to server");
//            throw new ServerDisconnectedException();
//        } catch (NotBoundException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        return;
    }
    private static void SocketGUI()
    {
        GUI.setConnectionType(false);
        GUI.launchGUI();
        System.exit(0);

    }
    private static void RMIGUI()
    {
        GUI.setConnectionType(true);
        GUI.launchGUI();
        System.exit(0);
    }


}
