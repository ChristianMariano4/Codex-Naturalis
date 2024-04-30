package it.polimi.ingsw.main;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args)
    {
        Scanner clientScanner = new Scanner(System.in);
        System.out.println("Insert client ip (nothing for localhost): ");
        String serverIp = clientScanner.nextLine();
        if(serverIp.isEmpty())
        {
            serverIp = "localhost";
        }
        try{
            Socket socket = new Socket(serverIp, GameValues.SERVER_PORT);
            System.out.println("test1");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while(true) {
                System.out.println("test2");

                ObjectiveCard card = (ObjectiveCard) in.readObject();
                System.out.println("test");

                System.out.println(card.getCardId());
                System.out.println("testsdfs");
            }

        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }


    }
}
