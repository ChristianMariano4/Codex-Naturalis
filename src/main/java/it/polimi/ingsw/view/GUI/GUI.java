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

/**
 * This class is responsible for the GUI of the application.
 * It extends the Application class from JavaFX.
 */
public class GUI extends Application {
    GUIController controller;
    Stage primaryStage;
    ViewGUI viewGUI;
    private static boolean isRMI;
    private double widthOld;
    private double heightOld;
    boolean resealable;
    private final String macName = "Mac OS X";
    private final String windowsName = "Windows";

    /**
     * Getter for the ViewGUI instance.
     *
     * @return The ViewGUI instance.
     */
    public ViewGUI getViewGUI() {
        return viewGUI;
    }

    /**
     * This method is called when the application is launched.
     * It initializes the server and switches to the nickname scene.
     *
     * @param stage The primary stage of the application.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * This method is used to switch between different scenes in the application.
     *
     * @param scene The scene to switch to.
     */
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

    /**
     * This method is used to set the connection type for the application.
     *
     * @param isRMI A boolean indicating whether the connection type is RMI.
     */
    public static void setConnectionType(boolean isRMI)
    {
        GUI.isRMI = isRMI;
    }

    /**
     * This method is used to launch the GUI.
     */
    public static void launchGUI() {
        launch();
    }

    /**
     * This method is used to initialize the server.
     *
     * @throws ServerDisconnectedException If the server is disconnected.
     */
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

    /**
     * This method is used to rescale the application window.
     *
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
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

    /**
     * This method is used to update the game state.
     *
     * @param update The object representing the update.
     */
    public void update(Object update)
    {
        controller.update(update);
    }

    /**
     * This method is called when a player reaches twenty points.
     *
     * @param username The username of the player who reached twenty points.
     */
    public void twentyPoints(String username) {
        controller.twentyPoints(username);
    }

    /**
     * This method is called when the final round of the game starts.
     */
    public void finalRound()
    {
        controller.finalRound();
    }

    /**
     * This method is called when the game ends.
     * It switches the scene to the scoreboard.
     */
    public void gameEnd()
    {
        Platform.runLater(() -> switchScene(GUIScene.SCOREBOARD));
    }

    /**
     * This method is called when a chat message is received.
     *
     * @param message The received chat message.
     */
    public void chatMessage(String message)
    {
        controller.chatMessage(message);
    }
}
