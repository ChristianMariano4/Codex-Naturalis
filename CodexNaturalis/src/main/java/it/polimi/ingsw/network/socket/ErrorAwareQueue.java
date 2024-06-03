package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ErrorType;
import it.polimi.ingsw.exceptions.*;

import java.util.concurrent.BlockingQueue;

public class ErrorAwareQueue {

    private BlockingQueue<Object> queue;
    public ErrorAwareQueue(BlockingQueue<Object> queue)
    {
        this.queue = queue;
    }
    public Object take() throws InterruptedException, NotEnoughPlayersException, DeckIsEmptyException, InvalidCardPositionException, NotTurnException, RequirementsNotMetException, InvalidUsernameException, GameAlreadyStartedException, NotExistingPlayerException, GameNotFoundException {
        Object result = queue.take();
        if(result instanceof ErrorType)
        {
            switch ((ErrorType) result)
            {
                case NOT_ENOUGH_PLAYERS ->
                    throw new NotEnoughPlayersException();
                case DECK_IS_EMPTY ->
                    throw new DeckIsEmptyException();
                case INVALID_CARD_POSITION ->
                    throw new InvalidCardPositionException();
                case NOT_YOUR_TURN ->
                    throw new NotTurnException();
                case REQUIREMENTS_NOT_MET ->
                    throw new RequirementsNotMetException();
                case INVALID_USERNAME ->
                    throw new InvalidUsernameException();
                case GAME_ALREADY_STARTED ->
                    throw new GameAlreadyStartedException();
                case NOT_EXISTING_PLAYER ->
                    throw new NotExistingPlayerException();
                case GAME_NOT_FOUND ->
                    throw new GameNotFoundException();
                case UNSPECIFIED ->
                    throw new RuntimeException();
                default -> throw new RuntimeException();
                }

        }
        return result;
    }

    public void put(Object object) throws InterruptedException {
        queue.put(object);
    }
}
