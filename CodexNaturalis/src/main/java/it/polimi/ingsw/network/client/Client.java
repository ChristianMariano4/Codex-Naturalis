package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.TUI.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Client extends UnicastRemoteObject implements ClientRMIInterface, Runnable {
    protected String username = null;
    protected boolean isGUI;
    private boolean isRMI = false;

    protected int gameId = -1; //invalid default value
    protected View view;
    protected boolean playing = false;
    protected Thread viewThread;
    protected boolean markerTurn = false;
    protected boolean markerDone = false;
    protected boolean starterCardAssigned = false;
    protected ArrayList<ObjectiveCard> objectiveCardsToChoose = null;
    protected boolean gameBegin = false;
    public long lastHeartbeat = System.currentTimeMillis();


    /**
     * Socket Constructor

     * @throws RemoteException
     */


    public Client(boolean isRMI) throws RemoteException {
        super();
        this.isRMI = isRMI;
    }
    public boolean isRMI()
    {
        return this.isRMI;
    }

    public abstract Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException;
    public abstract ArrayList<Game> getAvailableGames() throws IOException, InterruptedException, ServerDisconnectedException;

    public abstract Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, RemoteException, GameNotFoundException;

   /* public ServerRMIInterface getServer() {
        return server;
    }
    */

    public String getUsername() {
        return this.username;
    }

    public abstract ArrayList<Integer> setReady() throws NotEnoughPlayersException, IOException, ServerDisconnectedException;
    public abstract void quitGame() throws IOException, ServerDisconnectedException;
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void update(GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException {

            switch (event) {
                case BOARD_UPDATED -> {
                    //TODO: print what happened
                    //TODO: print new board
                    view.boardUpdate((Game) gameUpdate);
                }
                case NEW_PLAYER -> {
                    view.update((Game) gameUpdate);
                    view.newPlayer((Game) gameUpdate);
                }
                case GAME_INITIALIZED -> {
                    view.update((Game) gameUpdate);
                    this.playing = true;

                }
                case GAME_BEGIN -> {
                    view.update((Game) gameUpdate);
                    this.gameBegin = true;
                }
                case TURN_EVENT -> {
                    view.update((Game) gameUpdate);
                    if(this.viewThread!=null)
                        this.viewThread.interrupt();
                }
                case SECRET_OBJECTIVE_CHOICE_REQUEST ->
                {
                    this.objectiveCardsToChoose = (ArrayList<ObjectiveCard>) gameUpdate;
                }
                case MARKER_EVENT ->
                {
                    Game game = (Game) gameUpdate;
                    view.update(game);
                    if(!this.markerTurn) {
                        String currentMarkerChoice = game.getListOfPlayers().get(4 - game.getAvailableMarkers().size()).getUsername(); //how many markers have already been chosen
                        if (username.equals(currentMarkerChoice))
                            this.markerTurn = true;
                    }
                }
                case MARKER_DONE ->
                {
                    view.update((Game) gameUpdate);
                    this.markerDone = true;
                }
                case ASSIGNED_STARTER_CARDS ->
                {
                    view.update((Game) gameUpdate);
                    this.starterCardAssigned = true;
                }
                case STARTER_CARD_SIDE_CHOSEN ->
                {
                    view.update((Game) gameUpdate);
                }
                case TWENTY_POINTS -> {
                    view.twentyPoints((String) gameUpdate);
                    if(viewThread!=null)
                        this.viewThread.interrupt();
                }
                case FINAL_ROUND ->
                {
                    view.finalRound();
                    if(viewThread!=null)
                        this.viewThread.interrupt();
                }
                case GAME_END ->
                {

                    view.update((Game) gameUpdate);
                    view.gameEnd();
                    if (this.viewThread != null) {
                        this.viewThread.interrupt();
                    }
                }
                case PLAYER_DISCONNECTED ->
                {
                    view.update((Game) gameUpdate);
                    if (this.viewThread != null) {
                        this.viewThread.interrupt();
                    }
                }
            }
    }

    boolean preGameStart(ViewCLI viewCLI) throws InterruptedException, NotExistingPlayerException, IOException, ServerDisconnectedException {
        int choice = viewCLI.setChoiceGame();
        if (choice == 0)
            return false;
        if (choice == 2)
            return true;
        viewCLI.setReady();
        while (!this.playing) {
            Thread.sleep(10);
        }
        this.markerTurn = viewCLI.waitingForMarkerTurn();
        while (!this.markerTurn) {
            Thread.sleep(10);
        }
        viewCLI.markerSelection();
        this.markerDone = !viewCLI.waitingForOthers();
        while (!this.markerDone) {
            Thread.sleep(10);
        }

        viewCLI.chooseStarterCardSide();
        while (this.objectiveCardsToChoose == null) {
            Thread.sleep(10);
        }
        viewCLI.showSharedObjectiveCards();
        viewCLI.chooseObjectiveCard(this.objectiveCardsToChoose);
        viewCLI.waitingForGameBegin();
        while (!this.gameBegin) {
            Thread.sleep(10);
        }
        return true;
    }
    public void resetClient()
    {
         this.gameId = -1;
         this.playing = false;
         this.viewThread = null;
         this.markerTurn = false;
         this.markerDone = false;
         this.starterCardAssigned = false;
         this.objectiveCardsToChoose = null;
         this.gameBegin = false;
    }
    protected void runTUI() throws ServerDisconnectedException, IOException, InterruptedException, NotExistingPlayerException {
        view = new ViewCLI(this);
        ViewCLI viewCLI = (ViewCLI) view;
        viewCLI.setUsername();
        while(true){
            while (preGameStart(viewCLI)) {
                this.viewThread = new Thread(viewCLI); //game loop actually begins here
                this.viewThread.start();
                this.viewThread.join();
                resetClient(); //resetting the client after end of game
                //TODO: add server side reset
            }
        }
    }
    public void serMarkerTurn(boolean turn)
    {
        this.markerTurn = turn;
    }
    public boolean getMarkerTurn()
    {

        return this.markerTurn;
    }

    public boolean getPlaying()
    {
        return this.playing;
    }

    public boolean getGameBegin()
    {
        return this.gameBegin;
    }
    protected void runGUI()
    {
            ViewGUI viewGUI = (ViewGUI) view;
            viewGUI.setClient(this);
    }

    public ArrayList<ObjectiveCard> getObjectiveCardsToChoose()
    {
        return this.objectiveCardsToChoose;
    }



    public abstract void setUsername(String username) throws IOException, ServerDisconnectedException, InvalidUsernameException;
    public abstract PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException, ServerDisconnectedException;
    public abstract PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws IOException, ServerDisconnectedException;
    public abstract StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws IOException, ServerDisconnectedException;
    public abstract CardInfo getCardInfo(Card card, int gameId) throws IOException, ServerDisconnectedException;
    public abstract void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, IOException, ServerDisconnectedException;
    public abstract void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException;
    public abstract Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, IOException, AngleAlreadyLinkedException, ServerDisconnectedException;
    public abstract void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, IOException, ServerDisconnectedException;
    public abstract void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, IOException, NotAvailableMarkerException, ServerDisconnectedException;
    public abstract void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, IOException, ServerDisconnectedException;
    public abstract HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException, IOException;
}
