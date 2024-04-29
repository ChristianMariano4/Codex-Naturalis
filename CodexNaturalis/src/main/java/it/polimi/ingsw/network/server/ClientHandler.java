package it.polimi.ingsw.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Clock;
import java.util.Date;

import static java.lang.Thread.sleep;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket,String name) {
        this.socket = socket;
        System.out.println(socket.toString() + " connected.");
        Thread.currentThread().setName(name);

    }


    @Override
    public void run() {
        try {
        PrintWriter outC = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader inC = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            outC.println("Test");
            while(true)
            {
                outC.println("name: " + Thread.currentThread().getName() + " " + new Date() + " -> " + socket.toString());
                sleep(5000);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
