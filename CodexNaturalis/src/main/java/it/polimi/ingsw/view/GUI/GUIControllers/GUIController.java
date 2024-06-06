package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public abstract class GUIController {

    protected ViewGUI viewGUI;
    protected GUI gui;
    protected Stage primaryStage;

    public void setView(ViewGUI view) {
        this.viewGUI = view;
    }
    public void setStage(Stage stage)
    {
        this.primaryStage = stage;
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void sceneInitializer() {

    }

    public void finalRound(){}

    public void update(Object update){}
    public void twentyPoints(String username) {

    }
    public void chatMessage(String message)
    {

    }
}
