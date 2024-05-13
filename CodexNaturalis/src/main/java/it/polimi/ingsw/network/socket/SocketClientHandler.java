package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.AbstractClientHandler;
import it.polimi.ingsw.network.rmi.GameHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.CreateGameMessage;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

//oggetto che fa da interfaccia con il client
//tramite cui mando i miei updates al client (come lo stub del client in RMI)
public class SocketClientHandler implements Runnable, AbstractClientHandler {

    private Socket socket;

    public SocketClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(true)
            {
                objectOutputStream.writeObject("hi!!!!"); // it works
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void update(GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException {

    }

    @Override
    public String getUsername() throws RemoteException {
        return null;
    }
}
