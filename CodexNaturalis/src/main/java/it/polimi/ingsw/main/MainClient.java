package it.polimi.ingsw.main;

import it.polimi.ingsw.view.TUI.ViewCLI;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
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
        catch (Exception e)
        {
            System.out.println("Invalid input, try again");
        }
        }while(true);
        scanner.close();



    }
    private static void SocketTUI()
    {
        return;
    }
    private static void RMITUI()
    {
        System.out.println("Connecting to RMI server...");
        String serverName = "Server";

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
            ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);
            Client client = new Client(server, true);
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

        return;
    }
    private static void SocketGUI()
    {
        return;
    }
    private static void RMIGUI()
    {
        return;
    }


}
