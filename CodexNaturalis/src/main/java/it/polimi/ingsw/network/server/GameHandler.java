package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.GameStatus;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.observer.EventManager;
import it.polimi.ingsw.network.observer.GameListener;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler implements Serializable {
    //TODO: check se il client ha i permessi giusti
    private Game game;
    private final Controller controller;
    private int readyPlayers = 0;
    private final List<ClientHandlerInterface> clients; //list of the clients related to this game
    private final EventManager eventManager;
    private final Server server;
    private final BlockingQueue<Boolean> threadUpdates = new LinkedBlockingQueue<>();
    private boolean isOpen = true;
    private boolean twentyPointsReached = false;
    private boolean finalRound = false;
    private final int desiredNumberOfPlayers;

    public GameHandler(int gameId, Server server, int desiredNumberOfPlayers){
        this.server = server;
        this.eventManager = new EventManager();
        this.controller = new Controller(eventManager, this);
        try {
            this.game = controller.createGame(gameId);
        } catch (InvalidConstructorDataException | CardNotImportedException | CardTypeMismatchException |
                 DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }
        this.desiredNumberOfPlayers = desiredNumberOfPlayers;
        this.clients = new ArrayList<>();

    }
    public boolean getIsOpen()
    {
        synchronized (this) {
            return this.isOpen;
        }
    }

    public Game getGame() {
        synchronized (this) {
            return game;
        }
    }

    public Game addPlayerToGame(int gameId, String username) throws RemoteException, GameAlreadyStartedException {
        synchronized (this) {
            System.out.println("Request to add player to game " + gameId + " received");
            Game game;
            if (!isOpen)
                throw new GameAlreadyStartedException();
            try {
                game = this.controller.addPlayerToGame(username, desiredNumberOfPlayers);
                if(clients.size() == desiredNumberOfPlayers)
                    isOpen = false;
            } catch (AlreadyExistingPlayerException e) {
                throw new RuntimeException(e);
            } catch (AlreadyMaxNumberOfPlayersException e) {
                //TODO: avvisare il client che non può entrare in questo game
                throw new RuntimeException(e);
            }
            eventManager.notify(GameEvent.NEW_PLAYER, this.game);
            return game;
        }
    }

    public void addClient(ClientHandlerInterface client) throws RemoteException {
        synchronized (this) {
            clients.add(client);

        }
    }

    public void removeClient(ClientHandlerInterface client) throws RemoteException {
        synchronized (this) {
            clients.remove(client);
        }
        if(clients.size() == 1) {
            new Thread(() -> {
                boolean isReconnected = false;
                    try {
                        for (int i = 0; i < 5; i++) {
                            Thread.sleep(GameValues.GAME_END_TIMEOUT/6);
                            if (clients.size() > 1){
                                isReconnected = true;
                                break;
                            }
                        }
                        if (!isReconnected) {
                            game.setIsGameEndedForDisconnection(true);
                            eventManager.notify(GameEvent.GAME_END, game);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }).start();
        }
    }

    public void notifyUpdate(GameEvent event, Game game){
        for(ClientHandlerInterface client : clients){
            try {
                client.update(event, game);
            } catch (InterruptedException | NotExistingPlayerException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Integer> setReady() throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException {
        synchronized (this) {
            readyPlayers++;
            if (readyPlayers == desiredNumberOfPlayers) {
                try {
                    this.game = controller.initializeGame();
                    this.game.setGameStatus(GameStatus.ALL_PLAYERS_READY);
                } catch (CardTypeMismatchException | InvalidConstructorDataException | CardNotImportedException |
                         DeckIsEmptyException | AlreadyExistingPlayerException | AlreadyMaxNumberOfPlayersException |
                         IOException |
                         UnlinkedCardException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }
                this.isOpen = false;
                eventManager.notify(GameEvent.GAME_INITIALIZED, this.game);
                for (Player player : game.getListOfPlayers()) {
                    ArrayList<ObjectiveCard> objectiveCardsToChoose = controller.takeTwoObjectiveCards();
                    HashMap<ClientHandlerInterface, String> usernames = new HashMap<>();
                    for (ClientHandlerInterface client : clients) {
                        usernames.put(client, client.getUsername());
                    }
                    ClientHandlerInterface client;
                    client = usernames.entrySet().stream().filter(c -> Objects.equals(player.getUsername(), c.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
                    client.update(GameEvent.SECRET_OBJECTIVE_CHOICE_REQUEST, objectiveCardsToChoose);
                }
            }
            ArrayList<Integer> playersInfo = new ArrayList<>();
            playersInfo.add(desiredNumberOfPlayers);
            playersInfo.add(readyPlayers);
            return playersInfo;
        }
    }

    public void subscribe(ClientHandlerInterface client, int gameId) throws IOException, GameAlreadyStartedException {
        synchronized (this) {
            if (isOpen)
                eventManager.subscribe(GameEvent.class, new GameListener(client, server));
            else
                throw new GameAlreadyStartedException();
        }
    }

    public void subscribe(GameSerializer gameSerializer) {
        synchronized (this) {
            eventManager.subscribe(GameEvent.class, gameSerializer);
        }
    }

    public Player playCard(Player player, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException {
        synchronized (this) {
            return controller.playCard(player, card, otherCard, orientation);
        }
    }
    public List<ClientHandlerInterface> getClients() {
        synchronized (this) {
            return clients;
        }
    }

    public void addReadyPlayer(){
        readyPlayers++;
    }

    public int getReadyPlayers(){
        return readyPlayers;
    }


    public void updateClient(ClientHandlerInterface client, GameEvent event, Game game){
        try {
            server.updateClient(client, event, game);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        //TODO: start listening for user input and then invoke a method in the controller
        //each instance of gameHandler is a listener, i.e. is subscribed in the Listeners HashMap in EventManager
    }


    public BlockingQueue<Boolean> getQueue()

    {
        synchronized (this) {
        return threadUpdates;
    }
    }

    public Controller getController() {
        synchronized (this) {
            return controller;
        }
    }

    public Player getPlayer(String username) throws NotExistingPlayerException {
        synchronized (this) {
            return this.game.getPlayer(username);
        }
    }

    public void setMarker(Player player, Marker marker) throws NotAvailableMarkerException, NotExistingPlayerException {
        synchronized (this) {
            if (!game.getAvailableMarkers().contains(marker)) {
                throw new NotAvailableMarkerException();
            }
            controller.setMarker(player, marker);
            eventManager.notify(GameEvent.MARKER_EVENT, game);
            for (Player p : game.getListOfPlayers()) {
                if (p.getMarker() == null)
                    return;

            }
            eventManager.notify(GameEvent.MARKER_DONE, game);
        }

    }

    public void turnEvent(String username) throws NotExistingPlayerException, CardTypeMismatchException {
        synchronized (this) {
            if (this.finalRound && game.getCurrentPlayer().equals(game.getListOfPlayers().getLast())) {
                controller.calculateAndUpdateFinalPoints();
                eventManager.notify(GameEvent.GAME_END, game);
                return;
            }
            controller.nextTurn(game.getPlayer(username));
            for (Player p : game.getListOfPlayers()) {
                if (p.getPoints() >= 20 && !twentyPointsReached) {
                    this.twentyPointsReached = true;
                    eventManager.notify(GameEvent.TWENTY_POINTS, p.getUsername());
                }
            }
            if (twentyPointsReached && game.getCurrentPlayer().equals(game.getListOfPlayers().getFirst())) //final round begins when first player is playing and a player has reached 20 points
            {
                this.finalRound = true;
                eventManager.notify(GameEvent.FINAL_ROUND, (Object) null);
            }

            eventManager.notify(GameEvent.TURN_EVENT, game);
        }

    }



    public void setStarterCardSide(Player player, StarterCard starterCard, Side side) throws NotExistingPlayerException {
        synchronized (this) {
            controller.initializeStarterCard(game.getPlayer(player.getUsername()), starterCard, side);
            eventManager.notify(GameEvent.STARTER_CARD_SIDE_CHOSEN, game);
        }
    }


    public void setSecretObjectiveCard(Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException {
        synchronized (this) {
            controller.setSecretObjectiveCard(player, chosenObjectiveCard);
            for (Player p : game.getListOfPlayers()) {
                if (p.getSecretObjective() == null)
                    return;
            }
            game.setGameStatus(GameStatus.GAME_STARTED);
            eventManager.notify(GameEvent.GAME_BEGIN, game);
        }
    }

    public void unsubscribe(String username) {
        GameListener toUnsubscribe = (GameListener) eventManager.getListener(GameEvent.class, (l) -> {
            GameListener gl = (GameListener) l;
            return gl.getUsername().equals(username);
        });
        synchronized (this) {
            eventManager.unsubscribe(GameEvent.class, toUnsubscribe);
        }
    }


    public void setPlayerDisconnected(String username) {
        synchronized (this) {
            try {
                controller.setPlayerDisconnected(username);
                if (game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    this.readyPlayers--;
                    game.removePlayer(username);
                } else if (game.getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    if(game.getPlayer(username).getIsTurn()){
//                        controller.finishTurn(game.getPlayer(username));
                        controller.nextTurn(game.getPlayer(username));
                        eventManager.notify(GameEvent.TURN_EVENT, game);
                    }
                }
            } catch (NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setRandomInitialization(String username) throws NotExistingPlayerException, NotAvailableMarkerException, DeckIsEmptyException {
    synchronized (this) {
            if (game.getPlayer(username).getMarker() == null){
                this.setMarker(game.getPlayer(username), game.getAvailableMarkers().getFirst());
            }
            if (game.getPlayer(username).getSecretObjective() == null){
                this.setSecretObjectiveCard(game.getPlayer(username), game.getObjectiveCardDeck().getTopCard());
            }
            if (game.getPlayer(username).getStarterCard() == null){
                this.setStarterCardSide(game.getPlayer(username), game.getPlayer(username).getStarterCard(), Side.FRONT);
            }
        }
    }
}
