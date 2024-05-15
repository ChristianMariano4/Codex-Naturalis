package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.RMIClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUIControllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.w3c.dom.events.EventException;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class GUI extends Application {

    Pane root;
    GUIController controller;
    Stage primaryStage;
    ViewGUI viewGUI;
    private static boolean isRMI;

    public ViewGUI getViewGUI() {
        return viewGUI;
    }
    @Override
    public void start(Stage stage) throws IOException {

        this.viewGUI = new ViewGUI(this);
        do {


            try {
                serverInit();
                break;
            }
            catch(ServerDisconnectedException e)
            {
                continue;
        }
    }while(true);

        root = new Pane();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
            root = loader.load();
            controller = loader.getController();
            controller.setView(viewGUI);
            controller.setGUI(this);
        } catch (EventException e) {
            System.out.println("nope");
        }
        Scene scene = new Scene(root);

        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void setConnectionType(boolean isRMI)
    {
        GUI.isRMI = isRMI;
    }
    public static void launchGUI() {
        launch();
    }

    public void serverInit() throws ServerDisconnectedException {
        if(isRMI) {
            String serverName = "Server";

            try {
                System.out.println("Insert server IP address, leave empty for localhost: ");
                Scanner scanner = new Scanner(System.in);
                String serverIP = scanner.nextLine();
                if (serverIP.equals(""))
                    serverIP = "localhost";
                System.out.println("Connecting to RMI server...");
                Registry registry = LocateRegistry.getRegistry(serverIP, GameValues.RMI_SERVER_PORT);
                ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);

                RMIClient client = new RMIClient(server, this);
                Thread clientThread = new Thread(client);
                clientThread.start();

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                System.out.println("Insert server IP address, leave empty for localhost: ");
                Scanner scanner = new Scanner(System.in);
                String serverIP = scanner.nextLine();
                if(serverIP.equals(""))
                    serverIP = "localhost";
                Socket serverSocket = new Socket(serverIP, GameValues.SOCKET_SERVER_PORT);
                System.out.println("Connected to sever successfully");

                SocketClient client = new SocketClient(serverSocket, this);
                Thread clientThread = new Thread(client);
                clientThread.start();

            }
            catch(IOException e) {
                System.err.println("Couldn't connect to server");
                throw new ServerDisconnectedException();
            }
        }
    }
    public void displayErrorMessage()
    {
        System.out.println("Wrong input");
        //TODO: add error gui message
    }

    /*
    private void setupLoginHandlers() {
        loginButton.setOnMouseClicked(e -> {
            System.out.println("Login button clicked");
            //TODO: send login request to server
        });
    }*/
}
