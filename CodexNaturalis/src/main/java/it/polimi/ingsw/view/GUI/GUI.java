package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.RMIClient;
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

    public ViewGUI getViewGUI() {
        return viewGUI;
    }
    @Override
    public void start(Stage stage) throws IOException {

        this.viewGUI = new ViewGUI(this);

        serverInit();

        root = new Pane();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
            root = loader.load();
            controller = loader.getController();
            controller.setView(viewGUI);
        } catch (EventException e) {
            System.out.println("nope");
        }
        Scene scene = new Scene(root);

        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void launchGUI() {
        launch();
    }

    public void serverInit() {
        System.out.println("Connecting to RMI server...");
        String serverName = "Server";

        try {
            System.out.println("Insert server IP address: ");
            Scanner scanner = new Scanner(System.in);
            String serverIP = scanner.nextLine();
            Registry registry = LocateRegistry.getRegistry(serverIP,  GameValues.RMI_SERVER_PORT);
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

    /*
    private void setupLoginHandlers() {
        loginButton.setOnMouseClicked(e -> {
            System.out.println("Login button clicked");
            //TODO: send login request to server
        });
    }*/
}
