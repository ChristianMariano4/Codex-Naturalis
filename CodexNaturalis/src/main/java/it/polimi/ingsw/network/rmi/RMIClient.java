package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements ClientRMIInterface, Runnable {
    private String username;

    private Player player;
    private final ServerRMIInterface server;
    private int gameId;
    private View view;
    private boolean playing = false;



    public RMIClient(ServerRMIInterface server) throws RemoteException{
        this.server = server;
        //eventManager.subscribe(UserInputEvent.class, new UserInputListener(this));
    }

    public Game createGame(String username){
        try {
            this.gameId = server.createGame(this);
            server.subscribe(this, this.gameId);
            return server.addPlayerToGame(this.gameId, username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAvailableGames(){
        try {
            return server.getAvailableGames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Game joinGame(int gameId, String username){
        this.gameId = gameId;
        try {
            server.subscribe(this, this.gameId);
            return server.addPlayerToGame(this.gameId, username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerRMIInterface getServer() {
        return server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public int setReady(){
        try {
            return server.setReady(this.gameId, this.username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(GameEvent event, Game game) throws RemoteException {
        switch (event) {
            case BOARD_UPDATED -> {
                //TODO: print what happened
                //TODO: print new board
                view.boardUpdate(game);
                break;
            }
            case NEW_PLAYER -> {
                view.newPlayer(game);
                    break;
            }
            case GAME_BEGIN -> {
                //System.out.println(Thread.currentThread());
                this.playing = true;
                view.update(game);
                break;
            }
        }
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void run() {
        try {
            this.server.connect(this); //connect to the server
            view = new ViewCLI(this);
            ViewCLI viewCLI = (ViewCLI) view;
            viewCLI.setUsername();
            viewCLI.setChoiceGame();
            viewCLI.setReady();
            while(server.getQueue(gameId).isEmpty()); //this waits for gameHandler to add an element to the queue, signaling the beginning of the game

          /*  while(!this.playing)
            {
                Thread.sleep(1); //??????????????????????????????????''
            }*/
            viewCLI.gameBegin();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
