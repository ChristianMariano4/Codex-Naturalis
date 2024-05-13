package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.client.AbstractClientHandler;
import it.polimi.ingsw.network.messages.GameEvent;

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
