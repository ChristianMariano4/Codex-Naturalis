package it.polimi.ingsw.view.GUI;

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
    public void launchGUI() {
        launch();
    }

    /*
    private void setupLoginHandlers() {
        loginButton.setOnMouseClicked(e -> {
            System.out.println("Login button clicked");
            //TODO: send login request to server
        });
    }*/
}
