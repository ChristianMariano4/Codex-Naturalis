package it.polimi.ingsw.main;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.network.client.RMIClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.view.GUI.GUI;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The main class of the Client
 */
public class MainClient {

    /**
     * The main method of the class
     * @param args not used
     */
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
            //next iteration
        }
        catch (Exception e)
        {
            System.out.println("Invalid input, try again");
        }
        }while(true);
        scanner.close();
    }

    /**
     * Method used to start the Socket connection and the TUI
     * @throws ServerDisconnectedException if the server is disconnected
     */
    private static void SocketTUI() throws ServerDisconnectedException {
        System.out.println("Insert server IP address, leave empty for localhost: ");
        Scanner scanner = new Scanner(System.in);
        String serverIP = scanner.nextLine();
        if(serverIP.isEmpty())
            serverIP = "localhost";
        SocketClient client;
        try {
            client = new SocketClient(serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        client.connectToServer();
    }

    /**
     * Method used to start the RMI connection and the TUI
     * @throws ServerDisconnectedException if the server is disconnected
     */
    private static void RMITUI() throws ServerDisconnectedException {
        System.out.println("Insert server IP address, leave empty for localhost: ");
        Scanner scanner = new Scanner(System.in);
        String serverIP = scanner.nextLine();
        if(serverIP.isEmpty())
            serverIP = "localhost";
        System.out.println("Connecting to RMI server...");
        RMIClient client;
        try {
            client = new RMIClient(serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        client.connectToServer();
    }

    /**
     * Method used to launch the Socket connection and the GUI
     */
    private static void SocketGUI()
    {
        GUI.setConnectionType(false);
        GUI.launchGUI();
        System.exit(0);

    }

    /**
     * Method used to launch the RMI connection and the GUI
     */
    private static void RMIGUI()
    {
        GUI.setConnectionType(true);
        GUI.launchGUI();
        System.exit(0);
    }
}
