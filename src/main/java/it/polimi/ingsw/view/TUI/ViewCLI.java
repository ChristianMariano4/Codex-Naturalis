package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.network.client.Client;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

/**
 * This class represents the Command Line Interface (CLI) view of the game.
 * It implements the View interface and the Runnable interface.
 */
public class ViewCLI implements View, Runnable {
    private Game game;
    private final Client client;
    private final Scanner scanner = new Scanner(System.in);
    private String playerId;
    TUI ui = new TUI();
    private boolean inGame = true;
    private Thread readerThread;
    private String chatHistory;
    private Thread chatThread;
    private boolean inScanner = false;

    /**
     * Constructor for the ViewCLI class.
     * @param client The client object.
     */
    public ViewCLI(Client client) {
        this.client = client;
    }

    /**
     * This method is used to set the username of the player.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void setUsername() throws IOException, InterruptedException, ServerDisconnectedException {
        //TODO: remove println from view methods
        ui.setUsername();
        String username;
        do {
            username = scanner.nextLine();
            try {
                client.setUsername(username);
                break;
            } catch (InvalidUsernameException e) {
                System.out.println("Username not available, try again: ");
            }
        } while(true);
        playerId = username;
    }

    /**
     * This method is used to set the choice of the game.
     * @return 1 if the player creates a game, 2 if the player joins a game, 0 if the player quits the game.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public int setChoiceGame() throws ServerDisconnectedException {
        do {
            try {
                ui.createOrJoinAGame();
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    int numberOfPlayers;
                    do {
                        try {
                            ui.selectNumberOfPlayers();
                            numberOfPlayers = Integer.parseInt(scanner.nextLine());
                            if(numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                                break;
                            } else {
                                ui.invalidInput();
                            }
                        } catch(Exception e) {
                            ui.invalidInput();
                        }
                    } while(true);
                    this.game = client.createGame(this.client.getUsername(), numberOfPlayers);
                    System.out.println("Game created with id " + game.getGameId() + ".");
                    break;
                } else if (choice == 2) {
                    ui.showAllExistingGames(client.getAvailableGames());
                    this.game = client.joinGame(Integer.parseInt(scanner.nextLine()), this.client.getUsername());
                    if (game.getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                        return 2;
                    }
                    break;
                } else if(choice == 0) {
                    return 0;
                } else
                    throw new NumberFormatException();
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch(Exception e) {
                ui.invalidInput();
            }
        } while(true);
        return 1;
    }

    /**
     * This method is used to set the ready status of the player
     * @return true if the player is ready, false if the player quits the game
     * @throws ServerDisconnectedException if the server disconnects
     */
    public boolean setReady() throws ServerDisconnectedException{
        ArrayList<Integer> playersInfo;
        do{
            try {
                ui.setReady();
                int choice = Integer.parseInt(scanner.nextLine());
                if(choice == 1) {
                    try {
                        playersInfo = client.setReady();
                        ui.printReadyPlayersStatus(playersInfo);
                        break;
                    } catch (NotEnoughPlayersException e) {
                        ui.notEnoughPlayers();
                    }
                } else if(choice == 0) {
                    client.quitGame();
                    return false;
                } else {
                    throw new NumberFormatException();
                }
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch (Exception e) {
                ui.invalidInput();
            }
        } while(true);
        if(!Objects.equals(playersInfo.getFirst(), playersInfo.get(1))) {
            ui.readyConfirmation();
        }
        return true;
    }

    /**
     * This method is used to update the game state when a new player joins the game.
     */
    @Override
    public void newPlayer(Game gameUpdated) {
        this.game = gameUpdated;
    }

    /**
     * This method is used to update the game state.
     * @param gameUpdated The updated game state.
     */
    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
    }

    /**
     * This method is used to start the game loop.
     * @throws IOException If an I/O error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws InterruptedException If the thread is interrupted.
     * @throws CardTypeMismatchException If the card type does not match.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void gameLoop() throws IOException, NotExistingPlayerException, InterruptedException, CardTypeMismatchException, ServerDisconnectedException {

        ui.showMainScreen();
        Object lock = new Object();
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        AsyncReader reader = new AsyncReader(lock, blockingQueue);
        readerThread = new Thread(reader);
        readerThread.start();
        while(inGame) {
            boolean isTurn = game.getPlayer(client.getUsername()).getIsTurn();
            String playerPlaying = game.getCurrentPlayer().getUsername();
            boolean isStopped = game.getListOfPlayers().stream().filter(e -> !e.getUsername().equals(client.getUsername()) && !e.getIsDisconnected()).toList().isEmpty();
            if(game.getGameStatus().getStatusNumber() < GameStatus.GAME_STARTED.getStatusNumber() && game.getPlayer(client.getUsername()).getIsReconnecting()) {
                ui.waitingForGameBegin();
                while (game.getGameStatus().getStatusNumber() < GameStatus.GAME_STARTED.getStatusNumber()) {
                    Thread.sleep(10);
                }
            }
            if(!game.getIsGameEnded() && !game.getIsGameEndedForDisconnection()) {
                ui.showTurnScreen(playerPlaying, client.getUsername(), isStopped);
            } else if (game.getIsGameEndedForDisconnection()) {
                gameEndDisconnection();
            } else {
                showEndGameScreen();
            }
            while(!Thread.interrupted()) {
                String command;
                if(!blockingQueue.isEmpty()) {
                    synchronized (lock) {
                        command = blockingQueue.take();
                        if(game.getIsGameEnded()) {
                            if(chatThread != null)
                                chatThread.interrupt();
                            readerThread.interrupt();
                            lock.notifyAll();
                            return;
                        }
                        if(inGame) {
                            switch (command) {
                                case "q", "quit":
                                    client.quitGame();
                                    readerThread.interrupt();
                                    lock.notifyAll();
                                    return;
                                case "showPlayers":
                                    showAllPlayers();
                                    break;
                                case "myHand":
                                    showPlayerHand();
                                    break;
                                case "myResources":
                                    showMyResources();
                                    break;
                                case "showMyField":
                                    showPlayerField();
                                    break;
                                case "showOtherField":
                                    showOtherPlayerField();
                                    break;
                                case "scoreboard":
                                    showScoreboard();
                                    break;
                                case "showSharedObjectiveCards":
                                    showSharedObjectiveCards();
                                    break;
                                case "showSecretObjectiveCard":
                                    showSecretObjectiveCard();
                                    break;
                                case "playTurn":
                                    if (isTurn) {
                                        if(!isStopped) {
                                            playCard();
                                            if(!game.getTableTop().getDrawingField().getNoCardsLeft())
                                                drawCard();
                                            endTurn();
                                        } else {
                                            ui.gameIsStopped();
                                        }
                                    } else
                                        notYourTurn();
                                    break;
                                case "chat":
                                    chat();
                                    break;
                                default:
                                    try {
                                        int cardId = Integer.parseInt(command);
                                        PlayableCard cardFront = client.getPlayableCardById(game.getGameId(), cardId);
                                        PlayableCard cardBack = client.getOtherSideCard(game.getGameId(), cardFront);
                                        ui.showCardInfo(cardFront, client.getCardInfo(cardFront, game.getGameId()));
                                        ui.showCardInfo(cardBack, client.getCardInfo(cardBack, game.getGameId()));
                                    } catch (ServerDisconnectedException se) {
                                        throw se;
                                    } catch (Exception e) {
                                        ui.commandNotFound();
                                    }
                                    break;
                            }
                        } else {
                            readerThread.interrupt();
                            lock.notifyAll();
                            return;
                        }
                        lock.notifyAll();
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt
            }
        }
    }

    /**
     * This method is used to display a chat message.
     * @param message The chat message to be displayed.
     */
    @Override
    public void chatMessage(String message) {
        chatHistory += message + "\n";
    }

    /**
     * This method is used to start a chat.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     */
    private void chat() throws ServerDisconnectedException, IOException {
        ui.showChat(chatHistory);
        chatThread = new Thread(() ->
        {
            try {
                String oldChatHistory = this.chatHistory;
                while (!Thread.interrupted()) {
                    if (oldChatHistory.equals(chatHistory)) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            break;
                        }
                    } else {
                        ui.showNewMessage(chatHistory.substring(oldChatHistory.length()));
                        oldChatHistory = chatHistory;
                    }
                }
            }
            catch (Exception e)
            {
                //nothing
            }
        });
        chatThread.start();
        while(true) {
            inScanner = true;
            String message = scanner.nextLine();
            inScanner = false;
            if (message.equals("/exit") || game.getIsGameEnded()) {
                chatThread.interrupt();
                return;
            }
            if (!message.isBlank()) {
                client.sendChatMessage(game.getGameId(), message);
            }
        }
    }

    /**
     * This method is used to display the scoreboard.
     */
    private void showScoreboard() {
        ui.scoreboardTitle();
        for(Player player : game.getListOfPlayers()) {
            ui.showPoints(player);
        }
    }

    /**
     * This method is used to display the player's resources.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void showMyResources() throws NotExistingPlayerException {
        Player player = game.getPlayer(client.getUsername());
        LinkedHashMap<Resource, Integer> resources = new LinkedHashMap<>();
        for(Resource resource: Resource.values()) {
            if(resource.equals(Resource.NONE))
                continue;
            resources.put(resource,player.getResourceAmount(resource));
        }
        ui.showResources(resources);
    }

    /**
     * This method is used to inform the user that it's not their turn.
     */
    private void notYourTurn()
    {
        ui.notYourTurn(game.getCurrentPlayer().getUsername());
    }

    /**
     * This method is used to end the player's turn.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws IOException If an I/O error occurs.
     * @throws CardTypeMismatchException If the card type does not match.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    private void endTurn() throws NotExistingPlayerException, IOException, CardTypeMismatchException, ServerDisconnectedException {
        client.endTurn(game.getGameId(), client.getUsername());
    }

    /**
     * This method is used to draw a card.
     * @throws IOException If an I/O error occurs.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    private void drawCard() throws IOException, ServerDisconnectedException {
        HashMap<DrawPosition, GoldCard> discoveredGoldCards =  game.getTableTop().getDrawingField().getDiscoveredGoldCards();
        HashMap<DrawPosition, ResourceCard> discoveredResourceCards =  game.getTableTop().getDrawingField().getDiscoveredResourceCards();
        HashMap<GoldCard, CardInfo> uiDiscoveredGoldCards = new HashMap<>();
        HashMap<ResourceCard, CardInfo> uiDiscoveredResourceCards = new HashMap<>();
        for(GoldCard card: discoveredGoldCards.values()) {
            uiDiscoveredGoldCards.put(card, client.getCardInfo(card, game.getGameId()));
        }
        for(ResourceCard card: discoveredResourceCards.values()) {
            uiDiscoveredResourceCards.put(card, client.getCardInfo(card, game.getGameId()));
        }
        ui.showDiscoveredCards(uiDiscoveredGoldCards, uiDiscoveredResourceCards);
        ui.drawCard();
        do {
            try {
                inScanner = true;
                int choice = Integer.parseInt(scanner.nextLine());
                inScanner = false;
                if(game.getIsGameEnded())
                    return;
                CardType cardType = null;
                DrawPosition drawPosition = null;
                switch(choice) {
                    case 1:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.FROMDECK;
                        break;
                    case 2:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.FROMDECK;
                        break;
                    case 3:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.LEFT;
                        break;
                    case 4:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.RIGHT;
                        break;
                    case 5:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.LEFT;
                        break;
                    case 6:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.RIGHT;
                        break;
                    case 0:
                        break;
                    default: throw new NumberFormatException();
                }
                if(cardType == null) {
                    continue;
                }
                client.drawCard(game.getGameId(), client.getUsername(), cardType, drawPosition);
                break;
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch(Exception e) {
                ui.invalidInput();
            }
        }while(true);
    }

    /**
     * This method is used to play a card.
     * @throws IOException If an I/O error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    private void playCard() throws IOException, NotExistingPlayerException, ServerDisconnectedException {
        showPlayerHand();
        showPlayerField();
        do {
            try {
                ui.chooseCardToPlay();
                int cardId = Integer.parseInt(scanner.nextLine());
                PlayableCard card = game.getPlayer(client.getUsername()).getPlayerHand().getCardById(cardId);
                ui.chooseSide();
                inScanner = true;
                int side = Integer.parseInt(scanner.nextLine());
                inScanner = false;
                if(game.getIsGameEnded())
                    return;
                switch(side) {
                    case 1:
                        break;
                    case 2:
                        card = client.getOtherSideCard(game.getGameId(), card);
                        break;
                    default:
                        throw new NumberFormatException();
                }
                ui.chooseWhereToPlay();
                inScanner = true;
                int cardIdOnBoard = Integer.parseInt(scanner.nextLine());
                inScanner = false;
                if(game.getIsGameEnded())
                    return;
                PlayableCard cardOnBoard;
                do {
                    cardOnBoard = game.getPlayer(client.getUsername()).getPlayerField().getCardById(cardIdOnBoard);
                    ui.showCardInfo(cardOnBoard, client.getCardInfo(cardOnBoard, game.getGameId()));
                    ui.areYouSure();
                    inScanner = true;
                    String newChoice = scanner.nextLine();
                    inScanner = false;
                    if(game.getIsGameEnded())
                        return;
                    if(newChoice.equals("y"))
                        break;
                    cardIdOnBoard = Integer.parseInt(newChoice);
                } while(true);

                ui.chooseAngle();
                inScanner = true;
                int choice = Integer.parseInt(scanner.nextLine());
                inScanner = false;
                if(game.getIsGameEnded())
                    return;
                AngleOrientation orientation = switch (choice) {
                    case 1 -> AngleOrientation.TOPRIGHT;
                    case 2 -> AngleOrientation.TOPLEFT;
                    case 3 -> AngleOrientation.BOTTOMRIGHT;
                    case 4 -> AngleOrientation.BOTTOMLEFT;
                    case 0 -> AngleOrientation.NONE;
                    default -> throw new NumberFormatException();
                };
                if(orientation.equals(AngleOrientation.NONE)) {
                    continue;
                }
                client.playCard(game.getGameId(),client.getUsername(), cardOnBoard, card , orientation);
                break;
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch (RequirementsNotMetException e) {
                ui.requirementsNotMet();
            } catch(Exception e) {
                ui.invalidInput();
            }
        } while(true);
    }

    /**
     * This method is used to display the player's secret objective card.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws IOException If an I/O error occurs.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void showSecretObjectiveCard() throws NotExistingPlayerException, IOException, ServerDisconnectedException {
        ObjectiveCard card = game.getPlayer(client.getUsername()).getSecretObjective();
        ui.showCardInfo(card, client.getCardInfo(card, game.getGameId()));
    }

    /**
     * This method is used to display all players.
     */
    private void showAllPlayers() {
        ArrayList<String> usernames = new ArrayList<>();
        for(Player player : game.getListOfPlayers())
            usernames.add(player.getUsername());
        ui.showAllPlayers(usernames);
    }

    /**
     * This method is used to display the fact that the player has earned twenty points.
     */
    public void twentyPoints(String username)
    {
        ui.twentyPoints(username);
    }

    public void showSharedObjectiveCards() throws IOException, ServerDisconnectedException {
        HashMap<ObjectiveCard, CardInfo> sharedObjectiveCards = new HashMap<>();
        ArrayList<ObjectiveCard> objectiveCards = game.getTableTop().getSharedObjectiveCards();
        sharedObjectiveCards.put(objectiveCards.get(0), client.getCardInfo(objectiveCards.get(0), game.getGameId()));
        sharedObjectiveCards.put(objectiveCards.get(1), client.getCardInfo(objectiveCards.get(1), game.getGameId()));
        ui.showSharedObjectiveCard(sharedObjectiveCards);
    }

    /**
     * This method is used to choose an objective card.
     * @param objectiveCardsToChoose The objective cards to choose from.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws GameNotFoundException If the game is not found.
     */
    public void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) throws ServerDisconnectedException, GameNotFoundException {
            ui.secretObjectiveCardTitle();
            try {
                ui.showCardInfo(objectiveCardsToChoose.get(0), client.getCardInfo(objectiveCardsToChoose.get(0), game.getGameId()));
                ui.showCardInfo(objectiveCardsToChoose.get(1), client.getCardInfo(objectiveCardsToChoose.get(1), game.getGameId()));
                do {
                    try {
                        ui.chooseSecretObjectiveCard();
                        int choice = Integer.parseInt(scanner.nextLine());
                        ObjectiveCard chosenObjectiveCard = switch (choice) {
                            case 1 -> objectiveCardsToChoose.get(0);
                            case 2 -> objectiveCardsToChoose.get(1);
                            default -> throw new NumberFormatException();
                        };
                        if (chosenObjectiveCard == null)
                            throw new CardNotFoundException();
                        client.setSecretObjectiveCard(game.getGameId(), game.getPlayer(client.getUsername()), chosenObjectiveCard);
                        break;
                    } catch(ServerDisconnectedException se) {
                        throw se;
                    } catch(Exception e) {
                        if (game.getIsGameEnded())
                            throw new GameNotFoundException();
                        ui.invalidInput();
                    }
                } while(true);
             } catch(ServerDisconnectedException se) {
                throw se;
            } catch (GameNotFoundException e) {
                throw new GameNotFoundException();
            } catch (Exception e) {
                ui.somethingWentWrong();
            }
    }

    /**
     * This method is used to display the player's hand.
     * @throws IOException If an I/O error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void showPlayerHand() throws IOException, NotExistingPlayerException, ServerDisconnectedException {
        LinkedHashMap<PlayableCard, CardInfo> cardsInHand = new LinkedHashMap<>();
        for(PlayableCard card: game.getPlayer(client.getUsername()).getPlayerHand().getCardsInHand()) {
            CardInfo cardInfo = client.getCardInfo(card, game.getGameId());
            cardsInHand.put(card, cardInfo);
            PlayableCard otherSide = client.getOtherSideCard(game.getGameId(), card);
            cardsInHand.put(otherSide, client.getCardInfo(otherSide, game.getGameId()));
        }
        ui.showPlayerHand(cardsInHand);
    }

    /**
     * This method is used to display the player's field.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void showPlayerField() throws NotExistingPlayerException {
        ui.showPlayerField(createMatrixFromField(game.getPlayer(client.getUsername()).getPlayerField()));
    }

    /**
     * This method is used to display another player's field.
     */
    public void showOtherPlayerField() {
        this.showAllPlayers();
        ui.playerFieldChoice();
        Player player;
        do {
            try {
                inScanner = true;
                String username = scanner.nextLine();
                inScanner = false;
                if(game.getIsGameEnded())
                    return;
                player = game.getListOfPlayers().stream().filter(p1 -> p1.getUsername().equals(username)).findFirst().orElseThrow();
                break;
            } catch(Exception e) {
                ui.wrongUsername();
            }
        } while(true);
        ui.showPlayerField(createMatrixFromField(player.getPlayerField()));
    }

    /**
     * This method is used to select a marker.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws GameNotFoundException If the game is not found.
     */
    public void markerSelection() throws ServerDisconnectedException, GameNotFoundException {
        ArrayList<Marker> markerList = game.getAvailableMarkers();
        ui.showAvailableMarkers(markerList);
        do{
            try {
                Marker chosenMarker;
                int choice = Integer.parseInt(scanner.nextLine());

                if(choice > markerList.size())
                    throw new NumberFormatException();
                chosenMarker = switch (choice) {
                    case 1 -> markerList.get(0);
                    case 2 -> markerList.get(1);
                    case 3 -> markerList.get(2);
                    case 4 -> markerList.get(3);
                    default -> throw new NumberFormatException();
                };
                client.setMarker(game.getPlayer(client.getUsername()), game.getGameId(), chosenMarker);
                break;
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch(Exception e) {
                if(game.getIsGameEnded()) {
                    throw new GameNotFoundException();
                }
                ui.invalidInput();
            }
        }while(true);
    }

    /**
     * This method is used to display the fact that the player is waiting for the marker turn.
     */
    public boolean waitingForMarkerTurn()
    {
        if(!game.getCurrentPlayer().getUsername().equals(client.getUsername())) {
            ui.waitingForMarkerTurn();
            return false;
        }
        return true;
    }

    /**
     * This method is used to create a matrix from a field.
     * @param playerField The player field.
     * @return The matrix created from the field.
     */
    private int[][] createMatrixFromField(PlayerField playerField) {
        int i = 0, j;
        int[][] matrix = new int[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(PlayableCard[] cards: playerField.getMatrixField()) {
            j=0;
            for(PlayableCard card: cards) {
                if(card != null) {
                    matrix[i][j] = card.getCardId();
                }
                j++;
            }
            i++;
        }
        return matrix;
    }

    /**
     * This method is used to display the end game screen.
     */
    public void showEndGameScreen() {
        LinkedHashMap<String, Integer> playersPlacement = new LinkedHashMap<>();
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        List<Integer> points = new ArrayList<>(game.getListOfPlayers().stream().map(Player::getPoints).toList());
        Collections.sort(points);
        Collections.reverse(points);
        for(Integer point: points)
        {
            sortedPlayers.addAll(game.getListOfPlayers().stream().filter(e -> (e.getPoints() == point)).toList());
        }
        for(Player p : sortedPlayers) {
            playersPlacement.put(p.getUsername(), p.getPoints());
        }
        if(this.playerId.equals(sortedPlayers.getFirst().getUsername()))
            ui.printWinner();
        ui.showEndGameScreen(playersPlacement);
    }

    /**
     * This method is used to display the fact that the player is waiting for the game to begin.
     */
    public void waitingForGameBegin()
    {
        ui.waitingForGameBegin();
    }

    /**
     * This method is used to display the fact that the player needs to choose a side for his starter card.
     */
    public void chooseStarterCardSide() throws NotExistingPlayerException, IOException, ServerDisconnectedException, GameNotFoundException {
        StarterCard cardFront = game.getPlayer(client.getUsername()).getStarterCard();
        StarterCard cardBack = client.getOtherSideCard(game.getGameId(), cardFront);

        ui.chooseStarterCardSide(cardFront,cardBack, client.getCardInfo(cardFront, game.getGameId()), client.getCardInfo(cardBack, game.getGameId()));
        do{
            try{
                Side side;
                int choice = Integer.parseInt(scanner.nextLine());
                side = switch (choice) {
                    case 1 -> Side.FRONT;
                    case 2 -> Side.BACK;
                    default -> throw new NumberFormatException();
                };
                client.setStarterCardSide(game.getGameId(), game.getPlayer(client.getUsername()), cardFront, side);
                break;
            } catch(ServerDisconnectedException se) {
                throw se;
            } catch(Exception e) {
                if(game.getIsGameEnded())
                    throw new GameNotFoundException();
                ui.invalidInput();
            }
        } while(true);
    }

    /**
     * This method is used to inform the user that the final round has started.
     */
    public void finalRound()
    {
        ui.finalRound();
    }

    /**
     * This method is used to run the game.
     */
    @Override
    public void run() {
        try {
            chatHistory = "Write a public message or /[player's username] to send a private message. Write /exit to exit chat.\n";
            this.gameLoop();
        } catch (NotExistingPlayerException | InterruptedException | IOException | CardTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            System.err.println("Server disconnected. Try again later.");
            System.exit(-1);
        }
    }

    /**
     * This method is used to inform the user that the game has ended due to disconnection.
     */
    public void gameEndDisconnection()
    {
        ui.printWinner();
        ui.gameEndDisconnection();
        if(chatThread != null)
            chatThread.interrupt();
        if(readerThread != null){
            while(readerThread.isAlive() || inScanner){
                try {
                    readerThread.interrupt();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

    }

    /**
     * This method is used to inform the user that the game has ended.
     */
    @Override
    public void gameEnd() {
        this.inGame = false;
        game.setIsGameEnded(true);
        if(game.getIsGameEndedForDisconnection()) {
            gameEndDisconnection();
        }else{
            showEndGameScreen();
        }
        //TODO: check if for win without disconnection the readerThread.isAlive() logic is needed
    }

    /**
     * This method is used to inform the user that they have quit the game.
     */
    public void gameQuit() {
        ui.showGameQuitScreen();
    }

    /**
     * This method is used to get the async reader thread.
     * @return The async reader thread.
     */
    public Thread getAsyncReader() {
        return readerThread;
    }
}
