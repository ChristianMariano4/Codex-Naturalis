package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.RMIClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUIControllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.w3c.dom.events.EventException;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GUI extends Application {

    //BorderPane root;
    GUIController controller;
    Stage primaryStage;
    ViewGUI viewGUI;
    private static boolean isRMI;
    HashMap<GUIScene, SceneData> scenes;
    private double widthOld;
    private double heightOld;
    boolean rescalable;

    public ViewGUI getViewGUI() {
        return viewGUI;
    }
    @Override
    public void start(Stage stage) throws IOException {
        this.viewGUI = new ViewGUI(this);
        primaryStage = new Stage();
        loadAllScenes();

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




        Image icon = new Image(getClass().getResourceAsStream("/images/CODEX_Rulebook_IT/01.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Codex Naturalis");

        switchScene(GUIScene.NICKNAME);
    }
    private void loadAllScenes()
    {
        this.scenes = new HashMap<>();
        Parent root;
        GUIController controller;
        for(GUIScene scene : GUIScene.values())
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(scene.getPath()));
                root = loader.load();
                controller = loader.getController();
                controller.setView(viewGUI);
                controller.setGUI(this);
                controller.setStage(primaryStage);
                scenes.put(scene, new SceneData(new Scene(root), controller));
            } catch (IOException e) {
                System.out.println("Not loaded");
                throw new RuntimeException(e);
            }

        }


    }
    public void switchScene(GUIScene scene)
    {
        rescalable = false;

        scenes.get(scene).getController().sceneInitializer();
        primaryStage.setScene(scenes.get(scene).getScene());


        this.controller = scenes.get(scene).getController();

        primaryStage.show();

        widthOld = primaryStage.getScene().getWidth();
        heightOld = primaryStage.getScene().getHeight();

        this.primaryStage.widthProperty().addListener(
                (obs, oldVal, newVal) -> {
                    rescale((double) newVal - 16, heightOld); // - 16
                }
        );
        this.primaryStage.heightProperty().addListener(
                (obs, oldVal, newVal) -> {
                    rescale(widthOld, (double) newVal - 39); // -39
                }
        );
        rescalable = true;

       // primaryStage.setHeight(heightOld + 39);
      //  primaryStage.setWidth(widthOld);




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
    public  void rescale(double width, double height) {
        if(rescalable) {
            double w = width / widthOld;
            double h = height / heightOld;

            widthOld = width;
            heightOld = height;
            Scale scale = new Scale(w, h, 0, 0);
            primaryStage.getScene().lookup("#scalable").getTransforms().add(scale);
        }
    }
    public void update(Object update)
    {
        controller.update(update);
    }
}
