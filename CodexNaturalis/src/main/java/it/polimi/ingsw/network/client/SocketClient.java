package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.socket.ErrorAwareQueue;
import it.polimi.ingsw.network.socket.SocketClientMessageHandler;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class SocketClient extends Client {
    private final Socket serverSocket;
    SocketClientMessageHandler messageHandler;
    Thread messageHandlerThread;
    ErrorAwareQueue messageHandlerQueue = new ErrorAwareQueue(new LinkedBlockingQueue<>());

    public SocketClient(Socket serverSocket) throws RemoteException {
        super(false);
        this.serverSocket = serverSocket;

    }

    public SocketClient(Socket serverSocket, GUI gui) throws RemoteException {
        super(false);
        this.serverSocket = serverSocket;
        this.view = gui.getViewGUI();
        this.isGUI = true;
    }
    @Override
    public void setUsername(String username) throws IOException, ServerDisconnectedException, InvalidUsernameException {
        try
        {
            messageHandler.sendMessage(ClientMessageType.SET_USERNAME, username);
            messageHandlerQueue.take();
        }
        catch (InvalidUsernameException e)
        {
            throw e;
        }
        catch (ServerDisconnectedException e)
        {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.username = username;
    }

    @Override
    public Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException {
        try
        {
            messageHandler.sendMessage(ClientMessageType.CREATE_GAME, numberOfPlayers);

            this.gameId = (int) messageHandlerQueue.take();

            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            messageHandlerQueue.take(); //success message
            messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);
            return (Game) messageHandlerQueue.take();
        }
        catch (ServerDisconnectedException e)
        {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Game> getAvailableGames() throws IOException, InterruptedException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.AVAILABLE_GAMES_REQUEST, (Object) null);
        try {
            return (ArrayList<Game>) messageHandlerQueue.take();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, GameNotFoundException {
        this.gameId = gameId;
        try {
            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            if(messageHandlerQueue.take().equals(true)){
                messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);

            }
            return (Game) messageHandlerQueue.take();
        }
        catch (ServerDisconnectedException e)
        {
            throw e;
        }catch (GameAlreadyStartedException e) {
            try
            {
                messageHandler.sendMessage(ClientMessageType.RECONNECT_PLAYER, this.gameId, this.username);
                return (Game) messageHandlerQueue.take();
            }
            catch (IOException ex)
            {
                throw new ServerDisconnectedException();
            }
            catch (NotExistingPlayerException ex)
            {
                throw ex;
            } catch (Exception ex)
            {
                throw new RuntimeException();
            }
        } catch (Exception e)
        {
            throw new RuntimeException();
        }


    }

    @Override
    public ArrayList<Integer> setReady() throws NotEnoughPlayersException, IOException, ServerDisconnectedException {
        try {
            messageHandler.sendMessage(ClientMessageType.SET_READY, this.gameId);
            return (ArrayList<Integer>) messageHandlerQueue.take();
        }
        catch(ServerDisconnectedException se)
        {
            throw se;
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
    public void quitGame() throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.QUIT_GAME, gameId);
        try {
            messageHandlerQueue.take();
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (Exception e)
        {
            throw new RuntimeException();
        }
    }


    @Override
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException {
        return null;//TODO!
    }

    @Override
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws IOException, ServerDisconnectedException {
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
    public StarterCard getOtherSideCard(int gameId, StarterCard starterCard) throws IOException, ServerDisconnectedException {
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
    public CardInfo getCardInfo(Card card, int gameId) throws IOException, ServerDisconnectedException {
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
    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, IOException, ServerDisconnectedException {

            messageHandler.sendMessage(ClientMessageType.END_TURN, gameId, username);
            try {
                messageHandlerQueue.take();
            } catch (Exception e) {
                throw new RuntimeException();
            }
    }

    @Override
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException {
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
    public Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, IOException, AngleAlreadyLinkedException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.PLAY_CARD,gameId, username, cardOnBoard, card, orientation);
        try
        {
            return (Player) messageHandlerQueue.take();
        }
        catch (InvalidCardPositionException | RequirementsNotMetException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, IOException, ServerDisconnectedException {
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
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, IOException, NotAvailableMarkerException, ServerDisconnectedException {
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
    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, IOException, ServerDisconnectedException {
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

    @Override
    public HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException, IOException {
        messageHandler.sendMessage(ClientMessageType.GET_READY_STATUS, gameId);
        try
        {
            return (HashMap<String, Boolean>) messageHandlerQueue.take();
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
            SocketClientMessageHandler messageHandler = new SocketClientMessageHandler(this, inputStream, outputStream, messageHandlerQueue);
            this.messageHandler = messageHandler;
            Thread messageHandlerThread = new Thread(messageHandler);
            this.messageHandlerThread = messageHandlerThread;
            messageHandlerThread.start();

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    messageHandler.sendMessage(ClientMessageType.HEARTBEAT, System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ServerDisconnectedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }, 0, GameValues.HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);


            if(isGUI) {

                runGUI();

            } else {

                runTUI();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            System.err.println("Disconnected from server");
            return;
        }



    }

    @Override
    public void setLastHeatbeat(long time) throws RemoteException {

    }

    @Override
    public long getLastHeartbeat() throws RemoteException {
        return 0;
    }
}
