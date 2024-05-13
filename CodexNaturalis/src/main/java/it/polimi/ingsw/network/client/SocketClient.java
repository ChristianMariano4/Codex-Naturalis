package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class SocketClient extends Client {
    private final Socket serverSocket;
    public SocketClient(Socket serverSocket) throws RemoteException {
        super(false);
        this.serverSocket = serverSocket;

    }

    @Override
    public Game createGame(String username) {
        return null;
    }

    @Override
    public List<Integer> getAvailableGames() {
        return null;
    }

    @Override
    public Game joinGame(int gameId, String username) {
        this.gameId = gameId;
        return null;
    }

    @Override
    public int setReady() throws NotEnoughPlayersException {
        return 0;
    }

    @Override
    public boolean checkUsername(String username) throws RemoteException {
        return false;
    }

    @Override
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException {
        return null;
    }

    @Override
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws RemoteException {
        return null;
    }

    @Override
    public StarterCard getOtherSideCard(int gameId, StarterCard starterCard) throws RemoteException {
        return null;
    }

    @Override
    public CardInfo getCardInfo(Card card, int gameId) throws RemoteException {
        return null;
    }

    @Override
    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, RemoteException {

    }

    @Override
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, RemoteException, AlreadyThreeCardsInHandException, DeckIsEmptyException {

    }

    @Override
    public void playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, RemoteException, AngleAlreadyLinkedException {

    }

    @Override
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, RemoteException {

    }

    @Override
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, RemoteException, NotAvailableMarkerException {

    }

    @Override
    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, RemoteException {

    }

    public void run()
    {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
            while(true) {
                System.out.println(objectInputStream.readObject().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
