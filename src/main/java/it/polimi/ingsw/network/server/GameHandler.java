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
import java.util.*;

/**
 * Server side representation of a single game, used to implement the multiple games additional feature
 * It contains the model and controller of a game
 */
public class GameHandler implements Serializable {
    private Game game;
    private final Controller controller;
    private int readyPlayers = 0;
    private final List<ClientHandlerInterface> clients; //list of the clients related to this game
    private final EventManager eventManager;
    private final Server server;
    private boolean isOpen = true;
    private boolean twentyPointsReached = false;
    private boolean finalRound = false;
    private final int desiredNumberOfPlayers;
    private final HashMap<String, Boolean> readyStatus = new HashMap<>();
    private final HashMap<String, ArrayList<ObjectiveCard>> assignedObjectiveCards = new HashMap<>();
    private boolean areDecksEmpty = false;

    /**
     * Constructor of GameHandler
     * @param gameId the id of the game
     * @param server a reference to the Server
     * @param desiredNumberOfPlayers the amount of players needed to start the game
     */
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

    /**
     * Get the ready status of the players
     * @return readyStatus HashMap
     */
    public HashMap<String, Boolean> getReadyStatus()
    {
        return new HashMap<>(readyStatus);
    }

    /**
     * Get the game associated with the GameHandler
     * @return the game model
     */
    public Game getGame() {
        synchronized (this) {
            return game;
        }
    }

    /**
     * Adds a players to the game after checking that the player can join
     * @param gameId the id of the game
     * @param username the username of the player
     * @return a reference to the game model
     * @throws GameAlreadyStartedException if the game has already started
     */
    public Game addPlayerToGame(int gameId, String username) throws GameAlreadyStartedException {
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
            } catch (AlreadyExistingPlayerException | AlreadyMaxNumberOfPlayersException e) {
                throw new RuntimeException(e);
            }
            eventManager.notify(GameEvent.NEW_PLAYER, this.game);
            return game;
        }
    }

    /**
     * Adds a client to the list of clients
     * @param client a reference to the ClientHandlerInterface to add
     */
    public void addClient(ClientHandlerInterface client){
        synchronized (this) {
            clients.add(client);

        }
    }

    /**
     * Removes  a client from the game if the client quits or is disconnected
     * Also ends the game if the client is the only one left and no other player reconnects
     * @param client a reference to the ClientHandlerInterface to remove
     */
    public void removeClient(ClientHandlerInterface client) {
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
                                server.removeGame(this);  // whenever GAME_END is sent the game handler has to be removed from the list otherwise players can still join a finished game
                            }
                            catch (NullPointerException ignored) {
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

    /**
     * When the player is ready to start the game this method is called
     * @param username the name of the ready player
     * @return two Integer values that are respectively the amount of player needed to start the game and the players currently ready
     * @throws IOException when it fails to communicate with the client
     */
    public ArrayList<Integer> setReady(String username) throws IOException{
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
                    ArrayList<ObjectiveCard> objectiveCardsToChoose;
                    try {
                        objectiveCardsToChoose = controller.takeTwoObjectiveCards();
                    } catch (DeckIsEmptyException e) {
                        throw new RuntimeException(e);
                    }
                    assignedObjectiveCards.put(player.getUsername(), objectiveCardsToChoose);
                    HashMap<ClientHandlerInterface, String> usernames = new HashMap<>();
                    for (ClientHandlerInterface client : clients) {
                        usernames.put(client, client.getUsername());
                    }
                    ClientHandlerInterface client;
                    client = usernames.entrySet().stream().filter(c -> Objects.equals(player.getUsername(), c.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
                    assert client != null;
                    try {
                        client.update(GameEvent.SECRET_OBJECTIVE_CHOICE_REQUEST, objectiveCardsToChoose);
                    } catch (NotExistingPlayerException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            ArrayList<Integer> playersInfo = new ArrayList<>();
            playersInfo.add(desiredNumberOfPlayers);
            playersInfo.add(readyPlayers);
            return playersInfo;
        }
    }

    /**
     * Observer pattern subscribe
     * @param client the client to subscribe to the game
     * @throws IOException when it fails to communicate with the client
     * @throws GameAlreadyStartedException when the game has already started
     */
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

    @Deprecated
    public void subscribe(GameSerializer gameSerializer) {
        synchronized (this) {
            eventManager.subscribe(GameEvent.class, gameSerializer);
        }
    }

     /**
     * Play a card from the player hand to the player field
     * @param player the player that has to play the card
     * @param cardOnField the card on the field where the card has to be played
     * @param cardInHand the card that has to be played from the hand
     * @param orientation the angle where the card has to be played
     * @return the player that has played the card
     * @throws InvalidCardPositionException if the card position is invalid
     * @throws CardTypeMismatchException if the card type doesn't match
     * @throws RequirementsNotMetException if the requirements are not met
     * @throws AngleAlreadyLinkedException if the angle is already linked
     * @throws NotExistingPlayerException if the player doesn't exist
     */
    public Player playCard(Player player, PlayableCard cardOnField, PlayableCard cardInHand, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException {
        synchronized (this) {
            return controller.playCard(player, cardOnField, cardInHand, orientation);
        }
    }

    /**
     * Draw a card from the drawing field adn check if the decks are empty
     * @param username the username of the player that has to draw the card
     * @param cardType the type of the card that has to be drawn
     * @param drawPosition the position from where the card has to be drawn
     * @throws DeckIsEmptyException if the deck is empty
     * @throws AlreadyThreeCardsInHandException if the player already has three cards in his hand
     * @throws NotExistingPlayerException if the player doesn't exist
     */
    public void drawCard(String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        synchronized (this) {
            controller.drawCard(game.getPlayer(username), cardType, drawPosition);
            if(game.getTableTop().getDrawingField().getBothDecksEmpty())
                areDecksEmpty = true;
        }
    }

    /**
     * Draw a random card when the player quits the game mid-turn
     * @param username the username of the player that has to draw the card
     */
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
                }catch (DeckIsEmptyException ignored)
                {
                } catch (NotExistingPlayerException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }

                try
                {
                    drawCard(username, CardType.GOLD,position);
                    return;
                }catch (DeckIsEmptyException ignored)

                {
                } catch (NotExistingPlayerException | AlreadyThreeCardsInHandException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Gets the clients of the game
     * @return the list of clients connected to the game
     */
    public List<ClientHandlerInterface> getClients() {
        synchronized (this) {
            return clients;
        }
    }

    /**
     * Gets the controller of the game
     * @return the controller of the game
     */
    public Controller getController() {
        synchronized (this) {
            return controller;
        }
    }

    /**
     * Gets a player from the game with the given username
     * @param username the username of the player
     * @return a reference to the player
     * @throws NotExistingPlayerException when the player is not found
     */
    public Player getPlayer(String username) throws NotExistingPlayerException {
        synchronized (this) {
            return this.game.getPlayer(username);
        }
    }

    /**
     * Sets the marker for the player
     * @param player the player that is choosing
     * @param marker the marker to choose
     * @throws NotAvailableMarkerException when the marker is not available
     * @throws NotExistingPlayerException when the player doesn't exist
     */
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

    /**
     * Updates the game when a player has played their turn
     * It also checks end game conditions
     * @param username the username of the player that has played the turn
     * @throws NotExistingPlayerException when the player doesn't exist
     */
    public void turnEvent(String username) throws NotExistingPlayerException {
        synchronized (this) {
            if (this.finalRound && game.getCurrentPlayer().equals(game.getListOfPlayers().stream().filter(e -> !e.getIsDisconnected() || e.getUsername().equals(username)).toList().getLast())) {
                try {
                    controller.calculateAndUpdateFinalPoints();
                } catch (CardTypeMismatchException e) {
                    throw new RuntimeException(e);
                }
                server.removeGame(this);
                eventManager.notify(GameEvent.GAME_END, game);
                return;
            }
            controller.nextTurn(game.getPlayer(username));
            if(!twentyPointsReached && areDecksEmpty)
            {
                this.twentyPointsReached = true;
                eventManager.notify(GameEvent.TWENTY_POINTS, (Object) null);
            }
            else {
                for (Player p : game.getListOfPlayers()) {
                    if (p.getPoints() >= 20 && !twentyPointsReached) {
                        this.twentyPointsReached = true;
                        eventManager.notify(GameEvent.TWENTY_POINTS, p.getUsername());
                    }
                }
            }
            if (twentyPointsReached && game.getCurrentPlayer().equals(game.getListOfPlayers().stream().filter(e -> !e.getIsDisconnected()).toList().getFirst())) //final round begins when first player is playing and a player has reached 20 points
            {
                this.finalRound = true;
                eventManager.notify(GameEvent.FINAL_ROUND, (Object) null);
            }

            eventManager.notify(GameEvent.TURN_EVENT, game);
        }

    }

    /**
     * Sets the side of the starter card for the player
     * @param player the player choosing the card
     * @param starterCard the starter card chosen
     * @param side the side of the card
     * @throws NotExistingPlayerException when the player doesn't exist
     */
    public void setStarterCardSide(Player player, StarterCard starterCard, Side side) throws NotExistingPlayerException {
        synchronized (this) {
            controller.initializeStarterCard(game.getPlayer(player.getUsername()), starterCard, side);
            eventManager.notify(GameEvent.STARTER_CARD_SIDE_CHOSEN, game);
        }
    }

    /**
     * Sets the secret objective card for the player
     * @param player the player choosing the card
     * @param chosenObjectiveCard the objective card chosen
     * @throws NotExistingPlayerException when the player doesn't exist
     */
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

    /**
     * Observer pattern unsubscribe
     * @param username the username of the player to unsubscribe
     */
    public void unsubscribe(String username) {
        GameListener toUnsubscribe = (GameListener) eventManager.getListener(GameEvent.class, (l) -> {
            GameListener gl = (GameListener) l;
            return gl.getUsername().equals(username);
        });
        synchronized (this) {
            eventManager.unsubscribe(GameEvent.class, toUnsubscribe);
        }
    }

    /**
     * Sets the player to disconnected when they quit
     * It checks the current game status and acts accordingly
     * @param username the username of the player that was disconnected
     */
    public void setPlayerDisconnected(String username) {
        synchronized (this) {
            try {
                controller.setPlayerDisconnected(username);
                if (game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    if(game.getGameStatus().getStatusNumber() == GameStatus.ALL_PLAYERS_JOINED.getStatusNumber())
                        game.setGameStatus(GameStatus.LOBBY_CREATED);
                    this.isOpen = true;
                    if(readyStatus.get(username))
                        readyPlayers--;
                    readyStatus.remove(username);
                    game.removePlayer(username);
                } else if (game.getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    if(game.getPlayer(username).getIsTurn()){
                        if(game.getPlayer(username).getPlayerHand().getCardsInHand().size() != 3)
                        {
                            drawRandomCard(username);
                        }
                        turnEvent(username);
                    }
                }
            } catch (NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Invoked when the player quits during the game setup
     * It gives the player a random marker, starter card and secret objective card
     * @param username the username of the player
     * @throws NotExistingPlayerException when the player doesn't exist
     */
    public void setRandomInitialization(String username) throws NotExistingPlayerException{
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
                            } catch (NotAvailableMarkerException | NotExistingPlayerException e) {
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
            //can't check if null because the card is assigned in game initialization, so we just assign it anyway
            this.setStarterCardSide(game.getPlayer(username), game.getPlayer(username).getStarterCard(), Side.FRONT);

        }
    }

    /**
     * Sends a chat message to the players that have to receive it
     * @param message the content of the message to be sent
     * @param sender the username of the sender
     */
    public void sendChatMessage(String message, String sender)
    {
        Calendar rightNow = Calendar.getInstance();
        int hourValue = rightNow.get(Calendar.HOUR_OF_DAY);
        int minuteValue = rightNow.get(Calendar.MINUTE);
        String hour = String.valueOf(hourValue);
        String minute = String.valueOf(minuteValue);
        if(hourValue < 10)
            hour = "0"+ hour;
        if(minuteValue < 10)
            minute = "0" + minute;

        if(message.charAt(0) == '/')
        {
            ArrayList<String> players = new ArrayList<>(game.getListOfPlayers().stream().filter(e -> !e.getIsDisconnected() && !e.getUsername().equals(sender)).map(Player::getUsername).toList());
            for(String player : players)
            {
                try {
                    if (message.substring(1, player.length() + 2).equals(player + " ")) {
                        String substring = message.substring(player.length() + 2);
                        if(substring.isEmpty())
                            break;
                        String chatMessage = "[PRIVATE] " + hour + ":" + minute + " " + sender + " to " + player + ": " + substring;
                        server.sendPrivateChatMessage(sender, player, chatMessage);
                        return;
                    }
                }
                catch (IndexOutOfBoundsException e)
                {
                    break;
                }
            }
        }
        String chatMessage = "[PUBLIC] " + hour + ":" + minute + " " + sender +": " + message;
        eventManager.notify(GameEvent.CHAT_MESSAGE, chatMessage);
    }

    /**
     * Reconnects a player after they quit the game
     * @param username the username of the player to reconnect
     * @throws NotExistingPlayerException  when the player was not part of the game before reconnecting
     */
    public void reconnectPlayerToGame(String username) throws NotExistingPlayerException {

            game.getPlayer(username).setReconnecting();
            game.getPlayer(username).setConnected();
            eventManager.notify(GameEvent.PLAYER_RECONNECTED, game);

    }
}
