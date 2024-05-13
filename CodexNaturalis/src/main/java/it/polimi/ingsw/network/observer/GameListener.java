package it.polimi.ingsw.network.observer;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;

import java.rmi.RemoteException;

public class GameListener implements Listener<GameEvent> {

    private Game updatedGame;
    private final ClientRMIInterface client;
    private final Server server;

    public GameListener(ClientRMIInterface client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void update(GameEvent event, Object... args) {
        try {
            server.updateClient(client, event, args[0]);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
        System.out.println("We are in GameListener update method");
    }
}
