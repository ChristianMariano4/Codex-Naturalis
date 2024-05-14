package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    double WIDTH = 1000;
    double HEIGHT = 800;
    private Stage primaryStage, popUpStage;
    private
    Button loginButton = new Button("Login");

    Label title;
    Label loginLabel;
    Label label;
    ImageView gameImage;
    @Override
    public void start(Stage stage) throws IOException {
        VBox loginScreen = new VBox();

        label = new Label("Welcome to Codex Naturalis!");

        Image img = new Image(ClientApp.class.getResource("/images/CODEX_Rulebook_IT/01.png").openStream());
        gameImage = new ImageView(img);
        gameImage.setFitHeight(400);
        gameImage.setFitWidth(400);
        gameImage.setSmooth(true);
        gameImage.setPreserveRatio(true);

        VBox loginBox = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setPadding( new Insets(10) );
        gridPane.setHgap( 5 );
        gridPane.setVgap( 10 );
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);
        //gridPane.setMinSize(WIDTH, HEIGHT);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);

        gridPane.setStyle("""
                -fx-max-height: 100%;
                -fx-max-width: 100%;
                """);

        loginLabel = new Label("Enter the IP address:");
        title = new Label("Title");

        title.setStyle("""
                -fx-font-size: 30px;
                -fx-text-alignment: center;
                """);

        TextField username = new TextField();
        loginBox.getChildren().addAll(loginLabel, username);
        loginScreen.getChildren().addAll(label, gameImage, loginBox);

        gridPane.add(title, 3, 1);
        gridPane.add(gameImage, 3, 5);
        gridPane.add(loginBox, 3, 6);
        gridPane.add(loginButton, 3, 7);
        gridPane.getColumnConstraints().addAll(column1, column2);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(loginBox, HPos.CENTER);
        GridPane.setHalignment(loginButton, HPos.CENTER);

        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);

        this.setupLoginHandlers();

        stage.setTitle("Codex Naturalis");
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
