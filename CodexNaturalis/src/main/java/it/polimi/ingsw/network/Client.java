package it.polimi.ingsw.network;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.TUI.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Client extends UnicastRemoteObject implements ClientRMIInterface, Runnable {
    private String username = null;
    private boolean isRMI = false;
    private final ServerRMIInterface serverRMIInterface;
    private int gameId = -1; //invalid default value
    private View view;
    private boolean playing = false;
    private Thread viewThread;
    private boolean markerTurn = false;
    private boolean markerDone = false;
    private boolean starterCardAssigned = false;
    private ArrayList<ObjectiveCard> objectiveCardsToChoose = null;
    private boolean gameBegin = false;


    /**
     * Socket Constructor
     * @param isRMI
     * @throws RemoteException
     */
    public Client(boolean isRMI) throws RemoteException{
        this.serverRMIInterface = null;
    }

    /**
     * RMI constructor
     * @param serverRMIInterface
     * @param isRMI
     * @throws RemoteException
     */
    public Client(ServerRMIInterface serverRMIInterface, boolean isRMI) throws RemoteException{
        this.serverRMIInterface = serverRMIInterface;
        this.isRMI = isRMI;
    }

    public Game createGame(String username){
        if(this.isRMI) {
            try {
                this.gameId = serverRMIInterface.createGame(this);
                serverRMIInterface.subscribe(this, this.gameId);
                Game game = serverRMIInterface.addPlayerToGame(this.gameId, username, this);
                //server.initializePlayersHand(this.gameId, this.player);
                return game;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //socket logic
            return null;
        }
    }

    public List<Integer> getAvailableGames(){
        if(this.isRMI) {
            try {
                return serverRMIInterface.getAvailableGames();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //Socket logic
             return null;
        }
    }

    public Game joinGame(int gameId, String username){
        this.gameId = gameId;
        if(this.isRMI) {
            try {
                serverRMIInterface.subscribe(this, this.gameId);
                Game game = serverRMIInterface.addPlayerToGame(this.gameId, username, this);
                return game;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            //socket logic
            return null;
        }
    }

   /* public ServerRMIInterface getServer() {
        return server;
    }

    */

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public int setReady() throws NotEnoughPlayersException {
        if(this.isRMI) {
            try {
                return serverRMIInterface.setReady(this.gameId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (NotExistingPlayerException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (DeckIsEmptyException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //socket logic
            return 0;
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public void update(GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException {
        switch (event) {
            case BOARD_UPDATED -> {
                //TODO: print what happened
                //TODO: print new board
                view.boardUpdate((Game) gameUpdate);
            }
            case NEW_PLAYER -> {
                view.newPlayer((Game) gameUpdate);
            }
            case GAME_INITIALIZED -> {
                view.update((Game) gameUpdate);
                this.playing = true;

            }
            case GAME_BEGIN ->
            {
                view.update((Game) gameUpdate);
                this.gameBegin = true;
            }
            case TURN_EVENT -> {
                view.update((Game) gameUpdate);
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
                this.viewThread.interrupt();
            }
            case FINAL_ROUND ->
            {
                view.finalRound();
                this.viewThread.interrupt();
            }
            case GAME_END ->
            {
                view.update((Game) gameUpdate);
                this.viewThread.interrupt();
                //view.gameEnd();
            }

        }
    }

    private boolean preGameStart(ViewCLI viewCLI) throws InterruptedException, NotExistingPlayerException, RemoteException {
        if (!viewCLI.setChoiceGame())
            return false;
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
        viewCLI.showSharedObjectiveCards();
        while (!this.starterCardAssigned) {
            Thread.sleep(10);
        }
        viewCLI.chooseStarterCardSide();
        while (this.objectiveCardsToChoose == null) {
            Thread.sleep(10);
        }
        viewCLI.chooseObjectiveCard(this.objectiveCardsToChoose);
        viewCLI.waitingForGameBegin();
        while (!this.gameBegin) {
            Thread.sleep(10);
        }
        return true;
    }
    private void resetClient(ViewCLI viewCLI)
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

    public void run() {
        if(this.isRMI) {
            try {
                this.serverRMIInterface.connect(this); //connect to the server
                view = new ViewCLI(this);
                ViewCLI viewCLI = (ViewCLI) view;
                viewCLI.setUsername(); //set only once per client, outside of loop
                //TODO: deal with back side of cards!

                while (true) {
                    if (!preGameStart(viewCLI))
                        break;
                    this.viewThread = new Thread(viewCLI); //game loop actually begins here
                    this.viewThread.start();
                    this.viewThread.join();
                    resetClient(viewCLI); //resetting the client after end of game
                    //TODO: add server side reset
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //socket logic
        }
    }

    public boolean checkUsername(String username) throws RemoteException {
        if(isRMI)
        {
            return serverRMIInterface.checkUsername(username);
        }
        else {
            //socket logic
            return false;
        }
    }
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException {
        if(isRMI) {
            return serverRMIInterface.getPlayableCardById(gameId, cardId);
        }
        else {
            //socket logic
            return null;
        }
    }
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws RemoteException {
        if(isRMI) {
        return serverRMIInterface.getOtherSideCard(gameId, playableCard);
        }
        else {
            //socket logic
            return null;
        }

    }
    public StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws RemoteException {
            if(isRMI) {

        return serverRMIInterface.getOtherSideCard(gameId, starterCard);

            }
            else {
                //socket logic
                return null;
            }

    }
    public CardInfo getCardInfo(Card card, int gameId) throws RemoteException {
        if(isRMI) {
        return serverRMIInterface.getCardInfo(card, gameId);
        }
        else {
            //socket logic
            return null;
        }
    }
    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, RemoteException {
        if(isRMI) {
            serverRMIInterface.endTurn(gameId, username);

        } else {
                //socket logic
        }
    }
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, RemoteException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        if(isRMI) {
            serverRMIInterface.drawCard(gameId,username,cardType,drawPosition);
        }
        else {
                    //socket logic
            }
    }
    public void playCard(int gameId,String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, RemoteException, AngleAlreadyLinkedException {
        if(this.isRMI)
        {
            serverRMIInterface.playCard(gameId,username,cardOnBoard, card, orientation);
        }
        else
        {
            //socket logic
        }

    }
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, RemoteException {
        if(isRMI)
        {
            serverRMIInterface.setSecretObjectiveCard(gameId,player,chosenObjectiveCard);
        }
        else {
            //socket logic
        }
    }
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, RemoteException, NotAvailableMarkerException {
        if(isRMI)
        {
            serverRMIInterface.setMarker(player, gameId, chosenMarker);
        }
        else
        {
            //socket logic
        }
    }

    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, RemoteException {
        if(isRMI)
        {
            serverRMIInterface.setStarterCardSide(gameId, player, cardFront, side);
        }
        else {
            //socket logic
        }
    }


}
