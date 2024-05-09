package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.RMIServer;

import java.rmi.RemoteException;

public class GameListener implements Listener<GameEvent> {

    private Game updatedGame;
    private final ClientRMIInterface client;
    private final RMIServer server;

    public GameListener(ClientRMIInterface client, RMIServer server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void update(GameEvent event, Object... args) {
        try {
            server.updateClient(client, event, (Game)args[0]);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("We are in GameListener update method");
    }
}
