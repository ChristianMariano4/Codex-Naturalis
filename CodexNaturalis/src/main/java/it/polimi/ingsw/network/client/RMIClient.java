package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RMIClient extends Client {
    private ServerRMIInterface serverRMIInterface;

//    public RMIClient(ServerRMIInterface serverRMIInterface, String serverIP) throws RemoteException {
//        super(true, serverIP);
//        this.serverRMIInterface = serverRMIInterface;
//        this.isGUI = false;
//    }
    public RMIClient(String serverIP) throws RemoteException {
        super(true, serverIP);
        this.isGUI = false;
    }

    public RMIClient(GUI gui, String serverIP) throws RemoteException {
        super(true, serverIP);
        this.view = gui.getViewGUI();
        this.isGUI = true;
    }



    @Override
    public Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException {
        try {
            this.gameId = serverRMIInterface.createGame(this, numberOfPlayers);
            serverRMIInterface.subscribe(this, this.gameId);
            //server.initializePlayersHand(this.gameId, this.player);
            return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        } catch (GameAlreadyStartedException | NotExistingPlayerException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Game> getAvailableGames() throws ServerDisconnectedException {
        try {
            return serverRMIInterface.getAvailableGames();
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    @Override
    public Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, RemoteException, GameNotFoundException {
        this.gameId = gameId;
        try {
                serverRMIInterface.subscribe(this, this.gameId);
                return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
            } catch (RemoteException e) {
                throw new ServerDisconnectedException();
            } catch (GameAlreadyStartedException e) {
                return serverRMIInterface.reconnectPlayerToGame(this.gameId, username, this);
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Integer> setReady() throws NotEnoughPlayersException, ServerDisconnectedException {
        try
        {
            return serverRMIInterface.setReady(this.gameId, this.username);
        } catch (NotExistingPlayerException | DeckIsEmptyException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ServerDisconnectedException();
        }
    }

    public HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException
    {
        try {
            return serverRMIInterface.getReadyStatus(this.gameId);
        }
        catch (RemoteException e)
        {
            throw new ServerDisconnectedException();
        }

    }

    @Override
    public void quitGame() throws ServerDisconnectedException {
        try {
            serverRMIInterface.quitGame(this.gameId, this);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connectToServer() throws ServerDisconnectedException {

        try {

            Registry registry = LocateRegistry.getRegistry(this.serverIP,  GameValues.RMI_SERVER_PORT);
            //RMIClient client = new RMIClient(server);
            this.serverRMIInterface = (ServerRMIInterface) registry.lookup(GameValues.SERVER_NAME);
            Thread clientThread = new Thread(this);
            clientThread.start();
            clientThread.join();
            //System.exit(0);
        } catch (RemoteException e) {
            System.err.println("Couldn't connect to server");
            throw new ServerDisconnectedException();
        } catch (NotBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return;
    }

    public void run()
    {
        try {
            this.serverRMIInterface.connect(this); //connect to the server

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    serverRMIInterface.sendHeartbeat(System.currentTimeMillis(), this);
                } catch (RemoteException e) {
                    System.err.println("Failed to send heartbeat to server");
                    recontactServer();

                    throw new RuntimeException(e);
                }
            }, 0, GameValues.HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);

            if(isGUI) {
                runGUI();
            } else {
                runTUI();
            }
        } catch (InterruptedException | NotExistingPlayerException | IOException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            System.err.println("Disconnected from server");
            System.exit(0);

//            this.serverRMIInterface = null;
//            for(int i = 0; i<GameValues.MAX_ATTEMPTS_RECONNECTION; i++) {
//                try {
//                    Thread.sleep(1000);
//                    connectToServer();
//                    break;
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                } catch (ServerDisconnectedException ex) {
//                    continue;
//                }
//            }
//            if(this.serverRMIInterface == null) {
//                System.err.println("Failed to reconnect to server. Try again later.");
//                System.exit(0);
//            }
//            return;
        }
    }

    private void recontactServer() {
        for(int i = 0; i<GameValues.MAX_ATTEMPTS_RECONNECTION; i++) {
            try {
                serverRMIInterface.sendHeartbeat(System.currentTimeMillis(), this);
                break;
            } catch (RemoteException e) {
                System.err.println("Failed to send heartbeat to server");
                if(i == GameValues.MAX_ATTEMPTS_RECONNECTION - 1) {
                    System.err.println("Failed to reconnect to server. Try again later.");
                    System.exit(0);
                }
                try {
                    Thread.sleep(1000); //TODO set the time
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void setUsername(String username) throws IOException, ServerDisconnectedException, InvalidUsernameException {
        try {
            serverRMIInterface.setUsername(username);
            this.username = username;
        }
        catch (IOException e)
        {
            throw new ServerDisconnectedException();
        }
    }

    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException, ServerDisconnectedException {
        try {
        return serverRMIInterface.getPlayableCardById(gameId, cardId);
    }
        catch (RemoteException e)
    {
        throw new ServerDisconnectedException();
    }
    }

    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws RemoteException, ServerDisconnectedException {
        try {
        return serverRMIInterface.getOtherSideCard(gameId, playableCard);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws RemoteException, ServerDisconnectedException {
        try {
        return serverRMIInterface.getOtherSideCard(gameId, starterCard);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public CardInfo getCardInfo(Card card, int gameId) throws RemoteException, ServerDisconnectedException {
        try {
        return serverRMIInterface.getCardInfo(card, gameId);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, RemoteException, ServerDisconnectedException {
        try {
        serverRMIInterface.endTurn(gameId, username);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, RemoteException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException {
        try {
        serverRMIInterface.drawCard(gameId,username,cardType,drawPosition);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, RemoteException, AngleAlreadyLinkedException, ServerDisconnectedException {
        try {
        return serverRMIInterface.playCard(gameId,username,cardOnBoard, card, orientation);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, RemoteException, ServerDisconnectedException {
        try {
        serverRMIInterface.setSecretObjectiveCard(gameId,player,chosenObjectiveCard);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, RemoteException, NotAvailableMarkerException, ServerDisconnectedException {
        try {
        serverRMIInterface.setMarker(player, gameId, chosenMarker);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }

    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, RemoteException, ServerDisconnectedException {
        try {
        serverRMIInterface.setStarterCardSide(gameId, player, cardFront, side);
    }
        catch (RemoteException e)
        {
        throw new ServerDisconnectedException();
        }
    }




}
