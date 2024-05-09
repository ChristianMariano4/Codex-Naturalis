package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements ClientRMIInterface, Runnable {
    private String username;

    private final ServerRMIInterface server;
    private int gameId;
    private View view;
    private boolean playing = false;
    private Thread viewThread;
    private boolean markerTurn = false;
    private boolean markerDone = false;
    private boolean starterCardAssigned = false;


    public RMIClient(ServerRMIInterface server) throws RemoteException{
        this.server = server;
        //eventManager.subscribe(UserInputEvent.class, new UserInputListener(this));
    }

    public Game createGame(String username){
        try {
            this.gameId = server.createGame(this);
            server.subscribe(this, this.gameId);
            Game game = server.addPlayerToGame(this.gameId, username);
            //server.initializePlayersHand(this.gameId, this.player);
            return game;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAvailableGames(){
        try {
            return server.getAvailableGames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Game joinGame(int gameId, String username){
        this.gameId = gameId;
        try {
            server.subscribe(this, this.gameId);
            Game game = server.addPlayerToGame(this.gameId, username);
            return game;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerRMIInterface getServer() {
        return server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public int setReady(){
        try {
            return server.setReady(this.gameId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(GameEvent event, Game game) throws RemoteException, InterruptedException, NotExistingPlayerException {
        switch (event) {
            case BOARD_UPDATED -> {
                //TODO: print what happened
                //TODO: print new board
                view.boardUpdate(game);
            }
            case NEW_PLAYER -> {
                view.newPlayer(game);
            }
            case GAME_INITIALIZED -> {
                view.update(game);
                this.playing = true;

            }
            case TURN_EVENT -> {
                view.update(game);
                this.viewThread.interrupt();
            }
            case MARKER_EVENT ->
            {
                view.update(game);
                if(!this.markerTurn) {
                    String currentMarkerChoice = game.getListOfPlayers().get(4 - game.getAvailableMarkers().size()).getUsername(); //how many markers have already been chosen
                    if (username.equals(currentMarkerChoice))
                        this.markerTurn = true;
                }
            }
            case MARKER_DONE ->
            {
                view.update(game);
                this.markerDone = true;
            }
            case ASSIGNED_STARTER_CARDS ->
            {
                view.update(game);
                this.starterCardAssigned = true;
            }
            case STARTER_CARD_SIDE_CHOSEN ->
            {
                view.update(game);
            }

        }
    }

    public void run() {
        try {
            this.server.connect(this); //connect to the server
            view = new ViewCLI(this);
            ViewCLI viewCLI = (ViewCLI) view;
            viewCLI.setUsername();
            viewCLI.setChoiceGame();
            viewCLI.setReady();

            while(!this.playing)
            {
               Thread.sleep(10);
            }
            this.markerTurn = viewCLI.waitingForMarkerTurn();
            while(!this.markerTurn)
            {
                Thread.sleep(10);
            }
            viewCLI.markerSelection();
            this.markerDone = !viewCLI.waitingForOthers();
            while(!this.markerDone)
            {
                Thread.sleep(10);
            }
            while(!this.starterCardAssigned)
            {
                Thread.sleep(10);
            }
            viewCLI.chooseStarterCardSide();
            Thread.sleep(10);
            this.viewThread = new Thread(viewCLI); //game loop actually begins here
            this.viewThread.start();

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

}
