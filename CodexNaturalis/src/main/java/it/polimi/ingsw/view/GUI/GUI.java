package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.RMIClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.view.GUI.GUIControllers.GUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;

import static it.polimi.ingsw.enumerations.GUIScene.NICKNAME;

public class GUI extends Application {

    //BorderPane root;
    GUIController controller;
    Stage primaryStage;
    ViewGUI viewGUI;
    private static boolean isRMI;
    private double widthOld;
    private double heightOld;
    boolean resealable;
    private final String macName = "Mac OS X";
    private final String windowsName = "Windows";


    public ViewGUI getViewGUI() {
        return viewGUI;
    }
    @Override
    public void start(Stage stage) throws IOException {

        if(System.getProperty("os.name").equals(macName)) {
            GameValues.WINDOW_HEIGHT = 546;
            GameValues.WINDOW_WIDTH = 920;
        } else if(System.getProperty("os.name").contains(windowsName)) {
            GameValues.WINDOW_HEIGHT = 559;
            GameValues.WINDOW_WIDTH = 936;
        }

        this.viewGUI = new ViewGUI(this);
        primaryStage = new Stage();

        do {
            try {
                serverInit();
                break;
            } catch (ServerDisconnectedException e) {
                //TODO: handle server disconnection
            }
        } while (true);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/CODEX_Rulebook_IT/01.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Codex Naturalis");
        switchScene(NICKNAME);
    }

    public void switchScene(GUIScene scene)
    {
        try {
            assert scene.getPath() != null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scene.getPath()));
            Parent root = loader.load();
            try {
                primaryStage.getScene().setRoot(root);
            }
            catch (NullPointerException e)
            {
                primaryStage.setScene(new Scene(root));
                primaryStage.getScene().setRoot(root);
            }
            resealable = false;

            this.controller = loader.getController();
            controller.setView(viewGUI);
            controller.setGUI(this);
            controller.setStage(primaryStage);
            Thread.sleep(1);
            controller.sceneInitializer();

            primaryStage.show();

            String windows11Name = "Windows 11";
            if(System.getProperty("os.name").equals(macName)) {
                widthOld = GameValues.WINDOW_WIDTH;
                heightOld = GameValues.WINDOW_HEIGHT - 28;
                resealable = true;
                rescale(primaryStage.getWidth(), primaryStage.getHeight() - 28);

                this.primaryStage.widthProperty().addListener(
                        (obs, oldVal, newVal) -> {
                            rescale((double) newVal, heightOld); // - 16
                        }
                );
                this.primaryStage.heightProperty().addListener(
                        (obs, oldVal, newVal) -> {
                            rescale(widthOld, (double) newVal - 28); // -39
                        }
                );
            } else if(System.getProperty("os.name").equals(windows11Name)) {
                widthOld = GameValues.WINDOW_WIDTH - 16;
                heightOld = GameValues.WINDOW_HEIGHT - 41;
                resealable = true;
                rescale(primaryStage.getWidth()- 14.4, primaryStage.getHeight() - 37);

                this.primaryStage.widthProperty().addListener(
                        (obs, oldVal, newVal) -> {
                            rescale((double) newVal - 14.4, heightOld); // - 16
                        }
                );
                this.primaryStage.heightProperty().addListener(
                        (obs, oldVal, newVal) -> {
                            rescale(widthOld, (double) newVal - 37); // -39
                        }
                );
            }
            else if(System.getProperty("os.name").contains(windowsName)) {
                widthOld = GameValues.WINDOW_WIDTH - 16;
                heightOld = GameValues.WINDOW_HEIGHT - 41;
                resealable = true;
                rescale(primaryStage.getWidth()- 16, primaryStage.getHeight() - 39);

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
            }

        } catch (Exception e)
        {
            System.exit(-1);
        }

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
            System.out.println("Insert server IP address, leave empty for localhost: ");
            Scanner scanner = new Scanner(System.in);
            String serverIP = scanner.nextLine();
            if (serverIP.isEmpty())
                serverIP = "localhost";
            System.out.println("Connecting to RMI server...");
            RMIClient client;
            try {
                client = new RMIClient(this, serverIP);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            client.connectToServer();
        }
        else {
            System.out.println("Insert server IP address, leave empty for localhost: ");
            Scanner scanner = new Scanner(System.in);
            String serverIP = scanner.nextLine();
            if(serverIP.isEmpty())
                serverIP = "localhost";
            SocketClient client;
            try {
                client = new SocketClient(this, serverIP);
            }
            catch(IOException e) {
                System.err.println("Couldn't connect to server");
                throw new ServerDisconnectedException();
            }
            client.connectToServer();
        }
    }
    public  void rescale(double width, double height) {
        if(resealable) {
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
    public void twentyPoints(String username) {
        controller.twentyPoints(username);
    }
    public void finalRound()
    {
        controller.finalRound();
    }
    public void gameEnd()
    {
        Platform.runLater(() -> switchScene(GUIScene.SCOREBOARD));
    }
    public void chatMessage(String message)
    {
        controller.chatMessage(message);
    }
}
