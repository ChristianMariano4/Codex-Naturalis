package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {
    private Stage primaryStage, popUpStage;
    private
    Button loginButton = new Button("Login");
    Label loginLabel;
    Label label;
    ImageView gameImage;
    @Override
    public void start(Stage stage) throws IOException {
        VBox loginScreen = new VBox();

        label = new Label("Welcome to Codex Naturalis!");

        Image img = new Image(ClientApp.class.getResource("/images/CODEX_Rulebook_IT/01.png").openStream());
        gameImage = new ImageView(img);
        gameImage.setFitHeight(128);
        gameImage.setFitWidth(128);
        gameImage.setSmooth(true);
        gameImage.setPreserveRatio(true);

        VBox loginBox = new VBox();
        loginLabel = new Label("Login");
        TextField username = new TextField();
        loginBox.getChildren().addAll(loginLabel, username, loginButton);

        loginScreen.getChildren().addAll(label, gameImage, loginBox);

        Scene scene = new Scene(loginScreen, 800, 800);

        this.setupLoginHandlers();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void launchGUI() {
        launch();
    }

    private void setupLoginHandlers() {
        loginButton.setOnMouseClicked(e -> {
            System.out.println("Login button clicked");
            //TODO: send login request to server
        });
    }
}
