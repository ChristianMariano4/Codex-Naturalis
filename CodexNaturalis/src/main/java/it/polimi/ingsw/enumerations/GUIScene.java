package it.polimi.ingsw.enumerations;

public enum GUIScene {
    NICKNAME,
    LOBBY,
    JOINGAME,
    GAMELOBBY, GAME;


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

        }
        return null;
    }
}
