package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.*;
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

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler implements Serializable {
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
    HashMap<String, Boolean> readyStatus = new HashMap<>();
    HashMap<String, ArrayList<ObjectiveCard>> assignedObjectiveCards = new HashMap<>();


    public GameHandler(int gameId, Server server, int desiredNumberOfPlayers){
        this.server = server;
        this.eventManager = new EventManager();
        this.controller = new Controller(this);
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

    public HashMap<String, Boolean> getReadyStatus()
    {
        return new HashMap<>(readyStatus);
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
                readyStatus.put(username, false);
                if(clients.size() == desiredNumberOfPlayers) {
                    game.setGameStatus(GameStatus.ALL_PLAYERS_JOINED);
                    isOpen = false;
                }
            } catch (AlreadyExistingPlayerException e) {
                throw new RuntimeException(e);
            } catch (AlreadyMaxNumberOfPlayersException e) {
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
        eventManager.notify(GameEvent.PLAYER_DISCONNECTED, game);
        synchronized (clients) {
            clients.remove(client);
        }
        if(clients.size() == 1) {
            if(game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_READY.getStatusNumber()) // waiting in lobby and a player leaves, we can just remove it from the game without handling reconnection
                return;
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
                            try {
                                game.setIsGameEndedForDisconnection(true);
                                eventManager.notify(GameEvent.GAME_END, game);
                                server.removeGame(this);  // whenever GAME_END is sent the gamehandler has to be removed from the list otherwise players can still join a finished game
                            }
                            catch (NullPointerException e)
                            {
                                return;
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }).start();
        } else if(clients.isEmpty()) {
            server.removeGame(this);
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

    public ArrayList<Integer> setReady(String username) throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException {
        synchronized (this) {
            readyPlayers++;
            readyStatus.put(username,true);
            if (readyPlayers == desiredNumberOfPlayers) {
                try {
                    this.game = controller.initializeGame();
                } catch (CardTypeMismatchException | InvalidConstructorDataException | CardNotImportedException |
                         DeckIsEmptyException | AlreadyExistingPlayerException | AlreadyMaxNumberOfPlayersException |
                         IOException |
                         UnlinkedCardException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }
                this.game.setGameStatus(GameStatus.ALL_PLAYERS_READY);
                this.isOpen = false;
                eventManager.notify(GameEvent.GAME_INITIALIZED, this.game);
                for (Player player : game.getListOfPlayers()) {
                    ArrayList<ObjectiveCard> objectiveCardsToChoose = controller.takeTwoObjectiveCards();
                    assignedObjectiveCards.put(player.getUsername(), objectiveCardsToChoose);
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

    public void subscribe(ClientHandlerInterface client) throws IOException, GameAlreadyStartedException {
        synchronized (this) {
            if (this.game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_JOINED.getStatusNumber()) {
                eventManager.subscribe(GameEvent.class, new GameListener(client, server));
            } else {
                try {
                    this.game.getPlayer(client.getUsername());
                    eventManager.subscribe(GameEvent.class, new GameListener(client, server));
                } catch (NotExistingPlayerException e) {
                    throw new GameAlreadyStartedException();
                }
            }
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

    public void drawCard(String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        synchronized (this) {
            controller.drawCard(game.getPlayer(username), cardType, drawPosition);
            //TODO: add deck empty game end logic
        }
    }

    public void drawRandomCard(String username)
    {
        synchronized (this)
        {
            for(DrawPosition position: DrawPosition.values())
            {
                try
                {
                    drawCard(username, CardType.RESOURCE,position);
                    return;
                }catch (DeckIsEmptyException e)
                {
                } catch (NotExistingPlayerException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }

                try
                {
                    drawCard(username, CardType.GOLD,position);
                    return;
                }catch (DeckIsEmptyException e)
                {
                } catch (NotExistingPlayerException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }
            }
            //TODO: call game end method because all decks are empty
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
                server.removeGame(this);
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
                    if(game.getGameStatus().getStatusNumber() == GameStatus.ALL_PLAYERS_JOINED.getStatusNumber())
                        game.setGameStatus(GameStatus.LOBBY_CREATED);
                    this.isOpen = true;
                    if(readyStatus.get(username) == true)
                        readyPlayers--;
                    readyStatus.remove(username);
                    game.removePlayer(username);
                } else if (game.getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    if(game.getPlayer(username).getIsTurn()){
                        if(game.getPlayer(username).getPlayerHand().getCardsInHand().size() != 3)
                        {
                            drawRandomCard(username);
                        }
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
            if (game.getPlayer(username).getMarker() == null) { //wait for my turn then select the marker
                new Thread(() ->
                {
                    while(true)
                    {
                        String currentMarkerChoice = game.getListOfPlayers().get(4 - game.getAvailableMarkers().size()).getUsername(); //how many markers have already been chosen
                        if (username.equals(currentMarkerChoice)) {
                            try {
                                this.setMarker(game.getPlayer(username), game.getAvailableMarkers().getFirst());
                            } catch (NotAvailableMarkerException e) {
                                throw new RuntimeException(e);
                            } catch (NotExistingPlayerException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }


                }).start();
            }
            if (game.getPlayer(username).getSecretObjective() == null) {
                this.setSecretObjectiveCard(game.getPlayer(username), assignedObjectiveCards.get(username).getFirst()); //assigning one of the two already selected objective cards for that player so that objective card deck cannot be empty
            }
            //can't check if null because the card is assigned in game initialization so we just assign it anyway
        this.setStarterCardSide(game.getPlayer(username), game.getPlayer(username).getStarterCard(), Side.FRONT);

        }
    }
}
