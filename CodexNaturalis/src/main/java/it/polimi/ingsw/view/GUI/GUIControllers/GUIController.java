package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

public class GUIController {

    protected ViewGUI viewGUI;
    protected GUI gui;

    public void setView(ViewGUI view) {
        this.viewGUI = view;
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void sceneInitializer() {

    }
    public void update(Object update){}
}
