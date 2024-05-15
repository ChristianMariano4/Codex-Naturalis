package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.view.GUI.GUIControllers.GUIController;
import javafx.scene.Scene;

public class SceneData {
    private Scene scene;
    private GUIController controller;
    public SceneData(Scene scene, GUIController controller)
    {
        this.scene = scene;
        this.controller = controller;
    }
    public Scene getScene()
    {
        return scene;
    }
    public GUIController getController()
    {
        return controller;
    }
}
