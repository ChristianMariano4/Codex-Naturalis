package it.polimi.ingsw.enumerations;

public enum GUIScene {
    NICKNAME,
    LOBBY;


    public String getPath()
    {
        switch (this){
            case NICKNAME -> {
                return "/fxml/LoginScreen.fxml";
            }
            case LOBBY -> {
                return "/fxml/Lobby.fxml";
            }

        }
        return null;
    }
}
