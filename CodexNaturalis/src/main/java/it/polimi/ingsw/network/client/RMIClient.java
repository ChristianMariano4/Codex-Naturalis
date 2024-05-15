package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.TUI.ViewCLI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class RMIClient extends Client {
    private final ServerRMIInterface serverRMIInterface;

    public RMIClient(ServerRMIInterface serverRMIInterface) throws RemoteException {
        super(true);
        this.serverRMIInterface = serverRMIInterface;
        this.isGUI = false;
    }

    public RMIClient(ServerRMIInterface serverRMIInterface, GUI gui) throws RemoteException {
        super(true);
        this.serverRMIInterface = serverRMIInterface;
        this.view = gui.getViewGUI();
        this.isGUI = true;
    }

    @Override
    public Game createGame(String username) {
        try {
            this.gameId = serverRMIInterface.createGame(this);
            serverRMIInterface.subscribe(this, this.gameId);
            //server.initializePlayersHand(this.gameId, this.player);
            return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (GameAlreadyStartedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getAvailableGames() {
        try {
            return serverRMIInterface.getAvailableGames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game joinGame(int gameId, String username) {
        this.gameId = gameId;
        try {
                serverRMIInterface.subscribe(this, this.gameId);
            return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (GameAlreadyStartedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int setReady() throws NotEnoughPlayersException {
        try
        {
            return serverRMIInterface.setReady(this.gameId);
        } catch (NotExistingPlayerException | DeckIsEmptyException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run()
    {
        try {
            this.serverRMIInterface.connect(this); //connect to the server

            if(isGUI) {
                ViewGUI viewGUI = (ViewGUI) view;
                viewGUI.setClient(this);

            } else {

                view = new ViewCLI(this);
                ViewCLI viewCLI = (ViewCLI) view;
                viewCLI.setUsername(); //set only once per client, outside of loop

                while (true) {
                    if (!preGameStart(viewCLI))
                        break;
                    this.viewThread = new Thread(viewCLI); //game loop actually begins here
                    this.viewThread.start();
                    this.viewThread.join();
                    resetClient(viewCLI); //resetting the client after end of game
                    //TODO: add server side reset
                }

            }
        } catch (InterruptedException | NotExistingPlayerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkUsername(String username) throws IOException {
        return serverRMIInterface.checkUsername(username);
    }

    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException {
        return serverRMIInterface.getPlayableCardById(gameId, cardId);
    }

    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws RemoteException {
        return serverRMIInterface.getOtherSideCard(gameId, playableCard);
    }

    public StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws RemoteException {
        return serverRMIInterface.getOtherSideCard(gameId, starterCard);
    }

    public CardInfo getCardInfo(Card card, int gameId) throws RemoteException {
        return serverRMIInterface.getCardInfo(card, gameId);
    }

    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, RemoteException {
        serverRMIInterface.endTurn(gameId, username);
    }

    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, RemoteException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        serverRMIInterface.drawCard(gameId,username,cardType,drawPosition);
    }

    public void playCard(int gameId,String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, RemoteException, AngleAlreadyLinkedException {
        serverRMIInterface.playCard(gameId,username,cardOnBoard, card, orientation);
    }

    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, RemoteException {
        serverRMIInterface.setSecretObjectiveCard(gameId,player,chosenObjectiveCard);
    }

    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, RemoteException, NotAvailableMarkerException {
        serverRMIInterface.setMarker(player, gameId, chosenMarker);
    }

    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, RemoteException {
        serverRMIInterface.setStarterCardSide(gameId, player, cardFront, side);
    }

}
