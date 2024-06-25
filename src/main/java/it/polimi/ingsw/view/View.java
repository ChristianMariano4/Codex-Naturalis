package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.GameNotFoundException;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;

/**
 * This interface represents the View of the game.
 * It provides methods to update the game state, display messages, and handle game events.
 */
public interface View  {

    /**
     * This method is used to add a new player to the game.
     * @param gameUpdated The updated game state.
     */
    void newPlayer(Game gameUpdated);

    /**
     * This method is used to update the game state.
     * @param gameUpdated The updated game state.
     */
    void update(Game gameUpdated);

    /**
     * This method is used to inform that a player has reached twenty points.
     * @param username The username of the player who reached twenty points.
     */
    void twentyPoints(String username);

    /**
     * This method is used to prompt the user to choose an objective card.
     * @param objectiveCardsToChoose The objective cards to choose from.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws GameNotFoundException If the game is not found.
     */
    void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) throws ServerDisconnectedException, GameNotFoundException;

    /**
     * This method is used to inform that the final round has started.
     */
    void finalRound();

    /**
     * This method is used to inform that the game has ended due to the disconnection of all the other players.
     */
    void gameEndDisconnection();

    /**
     * This method is used to inform that the game has ended.
     */
    void gameEnd();

    /**
     * This method is used to display a chat message.
     * @param message The chat message to be displayed.
     */
    void chatMessage(String message);
}