package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketConnectionHandler implements Runnable{
    private ServerSocket serverSocket;
    private Server server;
    public SocketConnectionHandler(ServerSocket serverSocket, Server server)
    {
        this.serverSocket = serverSocket;

        this.server = server;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketClientHandler socketClientHandler = new SocketClientHandler(socket, server);
                server.connect(socketClientHandler);
                executor.submit(socketClientHandler);
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }
}
