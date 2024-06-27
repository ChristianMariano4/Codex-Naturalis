package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the GUI scenes.
 */
public enum GUIScene {
    NICKNAME,
    LOBBY,
    JOINGAME,
    GAMELOBBY,
    GAME,
    SCOREBOARD;

/**
     * This method returns the path of the FXML file associated with the scene.
     * @return the path of the FXML file associated with the scene
     */
    public String getPath()
    {
        switch (this){
            case NICKNAME -> {
                return "/fxml/LoginScreen.fxml";
            }
            case LOBBY -> {
                return "/fxml/Lobby.fxml";
            }
            case JOINGAME -> {
                return "/fxml/JoinGameScreen.fxml";
            }
            case GAMELOBBY -> {
                return "/fxml/GameLobbyScreen.fxml";
            }
            case GAME -> {
                return "/fxml/MainGameScreen.fxml";
            }
            case SCOREBOARD -> {
                return "/fxml/ScoreboardScreen.fxml";
            }

        }
        return null;
    }
}
