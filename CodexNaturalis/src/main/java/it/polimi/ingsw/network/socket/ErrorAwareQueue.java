package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ErrorType;
import it.polimi.ingsw.exceptions.*;

import java.util.concurrent.BlockingQueue;

/**
 * This class is a wrapper for the BlockingQueue used by the SocketClient to receive messages from the SocketClientMessageHandler
 * The purpose of this class is to make the SocketClient able to receive error messages from the server
 */
public class ErrorAwareQueue {

    /**
     * The actual BlockingQueue used for messages
     */
    private final BlockingQueue<Object> queue;

    /**
     * Constructor of the class
     * @param queue the queue passed at instantiation
     */
    public ErrorAwareQueue(BlockingQueue<Object> queue)
    {
        this.queue = queue;
    }

    /**
     * The BlockingQueue take method with the additional feature of throwing exceptions based on the message received
     * @return the message received by the server
     * @throws InterruptedException when the server throws it on the requested method call
     * @throws NotEnoughPlayersException when the server throws it on the requested method call
     * @throws DeckIsEmptyException when the server throws it on the requested method call
     * @throws InvalidCardPositionException when the server throws it on the requested method call
     * @throws NotTurnException when the server throws it on the requested method call
     * @throws RequirementsNotMetException when the server throws it on the requested method call
     * @throws InvalidUsernameException when the server throws it on the requested method call
     * @throws GameAlreadyStartedException when the server throws it on the requested method call
     * @throws NotExistingPlayerException when the server throws it on the requested method call
     * @throws GameNotFoundException when the server throws it on the requested method call
     */
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
                default -> throw new RuntimeException();
                }

        }
        return result;
    }

    /**
     * The BlockingQueue put method
     * @param object the object to be put in the queue so that it can be sent to the user
     * @throws InterruptedException when BlockingQueue put method throws it
     */
    public void put(Object object) throws InterruptedException {
        queue.put(object);
    }
}
