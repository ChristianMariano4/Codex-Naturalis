package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements ClientRMIInterface, Runnable{
    private final ServerRMIInterface server;
    private int gameId;
    private View view;


    public RMIClient(ServerRMIInterface server) throws RemoteException{
        this.server = server;
    }

    public void createGame(String username){
        try {
            this.gameId = server.createGame();
            server.addPlayerToGame(this.gameId, username);
            server.addClientToGameHandler(this.gameId, this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(int gameId,String username){
        this.gameId = gameId;
        try {
            server.addPlayerToGame(this.gameId, username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerRMIInterface getServer() {
        return server;
    }

    @Override
    public void update(GameEvent event, Game game) throws RemoteException {
        view.update(event, game);
    }

    public void run() {
        try {
            this.server.connect(this); //connect to the server
            view = new ViewCLI(this);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
