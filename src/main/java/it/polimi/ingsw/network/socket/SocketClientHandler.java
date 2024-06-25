package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.ClientMessage;
import it.polimi.ingsw.network.messages.ServerMessage;
import it.polimi.ingsw.network.server.Server;

import java.io.*;
import java.net.Socket;


/**
 * Server side Class used to receive messages and sent messages to the SocketClient
 * Each SocketClient has its own thread of SocketClientHandler
 * This class implements ClientHandlerInterface, so it is effectively treated by the server as an instance of a client
 */
public class SocketClientHandler implements Runnable, ClientHandlerInterface {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Server server;
    private String username = null;

    /**
     * Constructor
     * @param socket the socket passed by the server
     * @param server the server
     * @throws IOException when ObjectOutputStream and ObjectInputStream can't be created
     */
    public SocketClientHandler(Socket socket, Server server) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.server = server;
    }


    /**
     * This runs the instance of SocketClientHandler and waits for incoming messages
     */
    @Override
    public void run() {
            while (true) {
                try {
                ClientMessage message = (ClientMessage) inputStream.readObject();
                parseMessage(message);
                }
                catch (IOException | ClassNotFoundException e) {
                    try {
                        inputStream.close();
                        outputStream.close();
                    } catch (IOException ex) {
                        break;
                    }
                    break;
                }
            }
        }

    /**
     * After receiving a message the message type is interpreted and used to choose the correct method call to the server
     * This method then calls the sendMessage method to send a reply to the SocketClient
     * When the server throws an exception, an error message is sent
     *
     * @param message the message received containing both its type and content
     * @throws IOException when a reply can't be sent back
     */
    private void parseMessage(ClientMessage message) throws IOException {
        ClientMessageType messageType = message.getMessageType();
        synchronized (this) {
            try {
                switch (messageType) {
                    case HEARTBEAT -> {
                        server.sendHeartbeat((long) message.getMessageContent()[0], this);
                        sendMessage(ServerMessageType.HEARTBEAT, System.currentTimeMillis());
                    }
                    case SET_USERNAME -> {
                        server.setUsername((String) message.getMessageContent()[0]);
                        this.username = (String) message.getMessageContent()[0];
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case CREATE_GAME -> sendMessage(ServerMessageType.GAME_CREATED, server.createGame(this, (int) message.getMessageContent()[0]));
                    case SUBSCRIBE -> {
                        server.subscribe(this, (int) message.getMessageContent()[0]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case ADD_PLAYER -> sendMessage(ServerMessageType.PLAYER_ADDED, server.addPlayerToGame((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], this));
                    case CHECK_USERNAME -> sendMessage(ServerMessageType.USERNAME_CHECK_RESULT, server.checkUsername((String) message.getMessageContent()[0]));
                    case AVAILABLE_GAMES_REQUEST -> sendMessage(ServerMessageType.AVAILABLE_GAMES, server.getAvailableGames());
                    case SET_READY -> sendMessage(ServerMessageType.SUCCESS, server.setReady((Integer) message.getMessageContent()[0], this.username));
                    case CARD_INFO_REQUEST -> {
                        System.out.println("Sent Card Info");
                        sendMessage(ServerMessageType.CARD_INFO, server.getCardInfo((Card) message.getMessageContent()[0], (int) message.getMessageContent()[1]));
                    }
                    case SET_MARKER -> {
                        server.setMarker((Player) message.getMessageContent()[0], (Integer) message.getMessageContent()[1], (Marker) message.getMessageContent()[2]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case OTHER_SIDE_STARTER_CARD_REQUEST -> sendMessage(ServerMessageType.OTHER_SIDE_STARTER, server.getOtherSideCard((int) message.getMessageContent()[0], (StarterCard) message.getMessageContent()[1]));
                    case OTHER_SIDE_PLAYABLE_CARD_REQUEST -> sendMessage(ServerMessageType.OTHER_SIDE_PLAYABLE, server.getOtherSideCard((int) message.getMessageContent()[0], (PlayableCard) message.getMessageContent()[1]));
                    case SET_STARTER_CARD_SIDE -> {
                        server.setStarterCardSide((int) message.getMessageContent()[0], (Player) message.getMessageContent()[1], (StarterCard) message.getMessageContent()[2], (Side) message.getMessageContent()[3]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case SET_SECRET_OBJECTIVE_CARD -> {
                        server.setSecretObjectiveCard((int) message.getMessageContent()[0], (Player) message.getMessageContent()[1], (ObjectiveCard) message.getMessageContent()[2]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case PLAY_CARD -> {
                        Player player = server.playCard((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], (PlayableCard) message.getMessageContent()[2], (PlayableCard) message.getMessageContent()[3], (AngleOrientation) message.getMessageContent()[4]);
                        sendMessage(ServerMessageType.PLAYED_CARD_SUCCESS, player);
                    }
                    case DRAW_CARD -> {
                        server.drawCard((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], (CardType) message.getMessageContent()[2], (DrawPosition) message.getMessageContent()[3]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case END_TURN -> {
                        server.endTurn((int) message.getMessageContent()[0], (String) message.getMessageContent()[1]);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case RECONNECT_PLAYER -> sendMessage(ServerMessageType.SUCCESS, server.reconnectPlayerToGame((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], this));
                    case GET_READY_STATUS -> sendMessage(ServerMessageType.READY_STATUS, server.getReadyStatus((int) message.getMessageContent()[0]));
                    case QUIT_GAME -> {
                        server.quitGame((int) message.getMessageContent()[0], this);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }
                    case GET_CARD_BY_ID -> sendMessage(ServerMessageType.REQUESTED_CARD, server.getPlayableCardById((int) message.getMessageContent()[0], (int) message.getMessageContent()[1]));
                    case SEND_CHAT_MESSAGE ->
                    {
                        server.sendChatMessage((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], username);
                        sendMessage(ServerMessageType.SUCCESS, true);
                    }

                }
            } catch (GameNotFoundException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.GAME_NOT_FOUND);
            } catch (InvalidCardPositionException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.INVALID_CARD_POSITION);
            } catch (GameAlreadyStartedException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.GAME_ALREADY_STARTED);
            } catch (NotExistingPlayerException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.NOT_EXISTING_PLAYER);
            } catch (RequirementsNotMetException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.REQUIREMENTS_NOT_MET);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (DeckIsEmptyException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.DECK_IS_EMPTY);

            } catch (InvalidUsernameException e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.INVALID_USERNAME);
            } catch (Exception e) {
                sendMessage(ServerMessageType.ERROR, ErrorType.UNSPECIFIED);
            }
        }
    }

    /**
     * This sends a message to the SocketClient through the ObjectOutputStream
     *
     * @param messageType    the type of server message to be sent
     * @param messageContent the content of the message to be sent
     * @throws IOException when the message can't be sent through the ObjectOutputStream
     */
    public void sendMessage(ServerMessageType messageType, Object... messageContent) throws IOException {
        synchronized (this) {
            outputStream.reset();
            outputStream.writeObject(new ServerMessage(messageType, messageContent));
        }
    }


    /**
     * When the server sends an update to a SocketClient it is done through this method, which sends a message with the UPDATE type
     *
     * @param event      the GameEvent the update refers to
     * @param gameUpdate the content of the update
     * @throws IOException                when the message can't be sent through
     * @throws NotExistingPlayerException thrown by superclass
     */
    @Override
    public void update(GameEvent event, Object gameUpdate) throws IOException,  NotExistingPlayerException {
        sendMessage(ServerMessageType.UPDATE, event, gameUpdate);
    }

    /**
     * Getter
     * @return the unique username associated with the client
     * @throws IOException thrown by superclass
     */
    @Override
    public String getUsername() throws IOException {
        return this.username;
    }
}
