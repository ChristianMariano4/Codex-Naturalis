package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.socket.ErrorAwareQueue;
import it.polimi.ingsw.network.socket.SocketClientMessageHandler;
import it.polimi.ingsw.view.TUI.ViewCLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient extends Client {
    private final Socket serverSocket;
    SocketClientMessageHandler messageHandler;
    Thread messageHandlerThread;
    ErrorAwareQueue messageHandlerQueue = new ErrorAwareQueue(new LinkedBlockingQueue<>());

    public SocketClient(Socket serverSocket) throws RemoteException {
        super(false);
        this.serverSocket = serverSocket;

    }
    @Override
    public void setUsername(String username) throws IOException {
        this.username = username;
        messageHandler.sendMessage(ClientMessageType.SET_USERNAME, this.username);
    }

    @Override
    public Game createGame(String username) {
        try
        {
            messageHandler.sendMessage(ClientMessageType.CREATE_GAME, null);

            this.gameId = (int) messageHandlerQueue.take();

            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            messageHandlerQueue.take(); //success message
            messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);
            return (Game) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getAvailableGames() throws IOException, InterruptedException {
        messageHandler.sendMessage(ClientMessageType.AVAILABLE_GAMES_REQUEST, null);
        try {
            return (List<Integer>) messageHandlerQueue.take();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public Game joinGame(int gameId, String username) {
        this.gameId = gameId;
        try {
            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            messageHandlerQueue.take();
            messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);

            return (Game) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException();

        }

    }

    @Override
    public int setReady() throws NotEnoughPlayersException, IOException {
        try {
            messageHandler.sendMessage(ClientMessageType.SET_READY, this.gameId);
            return (int) messageHandlerQueue.take();
        }
        catch(NotEnoughPlayersException e)
        {
            throw new NotEnoughPlayersException();
        }
         catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public boolean checkUsername(String username) throws IOException, InterruptedException {
        messageHandler.sendMessage(ClientMessageType.CHECK_USERNAME, username);
        try {
            return (boolean) messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }

    }

    @Override
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException {
        return null;
    }

    @Override
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws IOException {
        messageHandler.sendMessage(ClientMessageType.OTHER_SIDE_PLAYABLE_CARD_REQUEST,gameId, playableCard);
        try
        {
            return (PlayableCard) messageHandlerQueue.take();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public StarterCard getOtherSideCard(int gameId, StarterCard starterCard) throws IOException {
        messageHandler.sendMessage(ClientMessageType.OTHER_SIDE_STARTER_CARD_REQUEST,gameId, starterCard);
        try
        {
            return (StarterCard) messageHandlerQueue.take();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }

    }

    @Override
    public CardInfo getCardInfo(Card card, int gameId) throws IOException {
        messageHandler.sendMessage(ClientMessageType.CARD_INFO_REQUEST, card, gameId);
        try
        {
            return (CardInfo) messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, IOException {

            messageHandler.sendMessage(ClientMessageType.END_TURN, gameId, username);
            try {
                messageHandlerQueue.take();
            } catch (Exception e) {
                throw new RuntimeException();
            }
    }

    @Override
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        messageHandler.sendMessage(ClientMessageType.DRAW_CARD, gameId, username, cardType, drawPosition);
        try
        {
            messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, IOException, AngleAlreadyLinkedException {
        messageHandler.sendMessage(ClientMessageType.PLAY_CARD,gameId, username, cardOnBoard, card, orientation);
        try
        {
            messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, IOException {
        messageHandler.sendMessage(ClientMessageType.SET_SECRET_OBJECTIVE_CARD, gameId, player, chosenObjectiveCard);
        try
        {
            messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, IOException, NotAvailableMarkerException {
        messageHandler.sendMessage(ClientMessageType.SET_MARKER, player, gameId, chosenMarker);
        try
        {
            messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, IOException {
        messageHandler.sendMessage(ClientMessageType.SET_STARTER_CARD_SIDE ,gameId, player, cardFront, side);
        try
        {
            messageHandlerQueue.take();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    public void run()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream()); //OutputStream must be created before InputStream otherwise it doesn't work
            ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());
            SocketClientMessageHandler messageHandler = new SocketClientMessageHandler(this, inputStream , outputStream, messageHandlerQueue);

            this.messageHandler = messageHandler;
            Thread messageHandlerThread = new Thread(messageHandler);
            this.messageHandlerThread = messageHandlerThread;
            messageHandlerThread.start();
            view = new ViewCLI(this);
            ViewCLI viewCLI = (ViewCLI) view;
            viewCLI.setUsername();
            while (true) {
                if (!preGameStart(viewCLI))
                    break;
                this.viewThread = new Thread(viewCLI); //game loop actually begins here
                this.viewThread.start();
                this.viewThread.join();
                resetClient(viewCLI); //resetting the client after end of game
                //TODO: add server side reset
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
