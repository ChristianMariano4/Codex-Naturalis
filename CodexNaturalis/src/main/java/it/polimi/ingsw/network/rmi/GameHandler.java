package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.GameListener;
import it.polimi.ingsw.network.maybeUseful.RemoteLock;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler {
    //TODO: check se il client ha i permessi giusti
    private Game game;
    private Controller controller;
    private int readyPlayers = 0;
    private List<ClientRMIInterface> clients; //list of the clients related to this game
    private final EventManager eventManager;
    private final RMIServer server;
    private final RemoteLock waitingLock = new RemoteLock();
    private final BlockingQueue<Boolean> threadUpdates = new LinkedBlockingQueue<>();
    private boolean twentPointsReached = false;
    private boolean finalRound = false;


    public GameHandler(int gameId, RMIServer server){
        this.server = server;
        this.eventManager = new EventManager();
        this.controller = new Controller(eventManager, this);
        try {
            this.game = controller.createGame(gameId);
        } catch (InvalidConstructorDataException | CardNotImportedException | CardTypeMismatchException |
                 DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }
        this.clients = new ArrayList<>();

    }

    public Game getGame() {
        return game;
    }

    public Game addPlayerToGame(int gameId, String username) throws RemoteException {
        System.out.println("Request to add player to game " + gameId + " received");
        Game game;
        try {
            game = this.controller.addPlayerToGame(username);
        } catch (AlreadyExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (AlreadyFourPlayersException e) {
            //TODO: avvisare il client che non può entrare in questo game
            throw new RuntimeException(e);
        }
        eventManager.notify(GameEvent.NEW_PLAYER, this.game);
        return game;
    }

    public void addClient(ClientRMIInterface client) throws RemoteException {
        clients.add(client);
    }

    public void notifyUpdate(GameEvent event, Game game){
        for(ClientRMIInterface client : clients){
            try {
                client.update(event, game);
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int setReady() throws RemoteException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException {
        readyPlayers++;
        if(readyPlayers == this.game.getNumberOfPlayers()){

            if(readyPlayers < 2) {
                if(readyPlayers == 1)
                    readyPlayers--;
                throw new NotEnoughPlayersException();
            }
            try {
                this.game = controller.initializeGame();
            } catch (CardTypeMismatchException | InvalidConstructorDataException | CardNotImportedException |
                     DeckIsEmptyException | AlreadyExistingPlayerException | AlreadyFourPlayersException | IOException |
                     UnlinkedCardException | AlreadyThreeCardsInHandException e) {
                throw new RuntimeException(e);
            }
        }

        if(readyPlayers >= this.game.getNumberOfPlayers()){
            eventManager.notify(GameEvent.GAME_INITIALIZED, this.game);
            for(Player player : game.getListOfPlayers())
            {
                ArrayList<ObjectiveCard> objectiveCardsToChoose = controller.takeTwoObjectiveCards();
                HashMap<ClientRMIInterface, String> usernames = new HashMap<>();
                for(ClientRMIInterface client : clients)
                {
                    usernames.put(client, client.getUsername());
                }
                ClientRMIInterface client;
                client = usernames.entrySet().stream().filter(c -> Objects.equals(player.getUsername(), c.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
                client.update(GameEvent.SECRET_OBJECTIVE_CHOICE_REQUEST, objectiveCardsToChoose);
            }
        }
        return readyPlayers;
    }

    public void subscribe(ClientRMIInterface client, int gameId) throws RemoteException {
        eventManager.subscribe(GameEvent.class, new GameListener(client, server));
    }

    public void playCard(Player player, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException {
        controller.playCard(player, card ,otherCard, orientation);
    }
    public List<ClientRMIInterface> getClients() {
        return clients;
    }

    public void addReadyPlayer(){
        readyPlayers++;
    }

    public int getReadyPlayers(){
        return readyPlayers;
    }


    public void updateClient(ClientRMIInterface client, GameEvent event, Game game){
        try {
            server.updateClient(client, event, game);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        //TODO: start listening for user input and then invoke a method in the controller
        //each instance of gameHandler is a listener, i.e. is subscribed in the Listeners HashMap in EventManager
    }

    public RemoteLock getWaitingLock()
    {
        return waitingLock;
    }

    public BlockingQueue<Boolean> getQueue()
    {
        return threadUpdates;
    }

    public Controller getController() {
        return controller;
    }

    public Player getPlayer(String username) throws NotExistingPlayerException {
        return this.game.getPlayer(username);
    }

    public void setMarker(Player player, Marker marker) throws NotAvailableMarkerException, NotExistingPlayerException {
        if(!game.getAvailableMarkers().contains(marker)) {
            throw new NotAvailableMarkerException();
        }
        controller.setMarker(player, marker);
        eventManager.notify(GameEvent.MARKER_EVENT, game);
        for(Player p: game.getListOfPlayers())
        {
            if(p.getMarker()==null)
                return;

        }
        eventManager.notify(GameEvent.MARKER_DONE, game);
        assignStarterCards(); //this assigns starter cards to all players in order right after they are done with choosing their marker

    }
    private void assignStarterCards()
    {
        for(Player p : game.getListOfPlayers())
        {
            try {
                controller.giveStarterCard(p);
            } catch (DeckIsEmptyException | NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
        eventManager.notify(GameEvent.ASSIGNED_STARTER_CARDS, game);
    }

    public void turnEvent(String username) throws NotExistingPlayerException, CardTypeMismatchException {
        if(this.finalRound && game.getCurrentPlayer().equals(game.getListOfPlayers().getLast()))
        {
            controller.calculateAndUpdateFinalPoints();
            eventManager.notify(GameEvent.GAME_END, game);
            return;
        }
        controller.nextTurn(game.getPlayer(username));
        for(Player p : game.getListOfPlayers())
        {
            if(p.getPoints() >= 20 && !twentPointsReached) {
                this.twentPointsReached = true;
                eventManager.notify(GameEvent.TWENTY_POINTS, p.getUsername());
            }
        }
        if(twentPointsReached && game.getCurrentPlayer().equals(game.getListOfPlayers().getFirst())) //final round begins when first player is playing and a player has reached 20 points
        {
            this.finalRound = true;
            eventManager.notify(GameEvent.FINAL_ROUND, null);
        }

        eventManager.notify(GameEvent.TURN_EVENT, game);

    }



    public void setStarterCardSide(Player player, StarterCard starterCard, Side side) throws NotExistingPlayerException {
        controller.initializeStarterCard(game.getPlayer(player.getUsername()), starterCard, side);
        eventManager.notify(GameEvent.STARTER_CARD_SIDE_CHOSEN, game);
    }


    public void setSecretObjectiveCard(Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException {
        controller.setSecretObjectiveCard(player, chosenObjectiveCard);
        for(Player p : game.getListOfPlayers())
        {
            if(p.getSecretObjective() == null)
                return;
        }
        eventManager.notify(GameEvent.GAME_BEGIN, game);
    }
}
