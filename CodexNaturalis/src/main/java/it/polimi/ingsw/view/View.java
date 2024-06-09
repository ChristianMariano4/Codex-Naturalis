package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.GameNotFoundException;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;

public interface View  {
    void boardUpdate(Game gameUpdated);
    void newPlayer(Game gameUpdated);
    void update(Game gameUpdated);
    void twentyPoints(String username);
    void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) throws ServerDisconnectedException, GameNotFoundException;
    void finalRound();
    void gameEndDisconnection();
    void gameEnd();
    void chatMessage(String message);
}
