//package it.polimi.ingsw.network.socket;
//
//import it.polimi.ingsw.exceptions.CardNotImportedException;
//import it.polimi.ingsw.exceptions.CardTypeMismatchException;
//import it.polimi.ingsw.exceptions.DeckIsEmptyException;
//import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.GameValues;
//import it.polimi.ingsw.network.rmi.GameHandler;
//import it.polimi.ingsw.view.View;
//import it.polimi.ingsw.network.messages.GameEvent;
//import it.polimi.ingsw.network.messages.userMessages.CreateGameMessage;
//import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
//import it.polimi.ingsw.network.rmi.ClientRMIInterface;
//
//import java.io.*;
//import java.rmi.RemoteException;
//
//import static java.lang.Thread.sleep;
//
////oggetto che fa da interfaccia con il client
////tramite cui mando i miei updates al client (come lo stub del client in RMI)
//public class SocketClientHandler implements Runnable, ClientRMIInterface{
//
//    private GameHandler gameHandler;
//    private final ObjectInputStream input;
//    private final ObjectOutputStream output;
//    private View view;
//
//    public SocketClientHandler(GameHandler gameHandler,  ObjectInputStream input, ObjectOutputStream output) {
//        this.gameHandler = gameHandler;
//        this.input = input;
//        this.output = output;
//    }
//
//
//    public void createGame(CreateGameMessage message){
//        try {
//            this.gameHandler.getController().createGame(message.getGameId());
//        } catch (InvalidConstructorDataException e) {
//            throw new RuntimeException(e);
//        } catch (CardNotImportedException e) {
//            throw new RuntimeException(e);
//        } catch (CardTypeMismatchException e) {
//            throw new RuntimeException(e);
//        } catch (DeckIsEmptyException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void joinGame(int gameId,String username){
//        //TODO
//    }
//
//    @Override
//    public void run() {
//        try {
//            UserMessageWrapper message = (UserMessageWrapper) input.readObject();
//            while(true){
//                switch(message.getType()){
//                    case CREATE_GAME -> {
//                        gameHandler.getController().createGame(GameValues.numberOfGames++);
//                    }
//                }
//            }
//
//            //Game game = c.createGame();
//
//
//            //outC.writeObject(game); //game.getObjectiveCardDeck().getTopCard()
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (CardTypeMismatchException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidConstructorDataException e) {
//            throw new RuntimeException(e);
//        } catch (CardNotImportedException e) {
//            throw new RuntimeException(e);
//        } catch (DeckIsEmptyException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    public void update(GameEvent event, Game game) throws RemoteException {
//        //TODO: handling updates coming from the model and propagating them to the view
//    }
//}
