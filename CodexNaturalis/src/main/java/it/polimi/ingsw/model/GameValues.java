package it.polimi.ingsw.model;

/**
 * This class contains the values of the game that are used in multiple classes.
 */
public class GameValues {

    public static final int MAX_PLAYER_NUMBER = 4;
    public static final int MAX_CARD_IN_HAND = 3;
    public static final int DEFAULT_MATRIX_SIZE = 81;
    public static final int RMI_SERVER_PORT = 1234;
    public static final int SOCKET_SERVER_PORT = 4567;
    public static int numberOfGames = 0;
    public static final int HEARTBEAT_INTERVAL = 10000;
    public static final int HEARTBEAT_TIMEOUT = 20000;
    public static final int GAME_END_TIMEOUT = 60000;
}
