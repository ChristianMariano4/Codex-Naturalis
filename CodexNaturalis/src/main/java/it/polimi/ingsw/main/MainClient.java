package it.polimi.ingsw.main;

import it.polimi.ingsw.model.GameValues;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                System.out.println(in.readLine());
            }

        }
        catch (Exception e)
        {
            System.err.println("Client error");
        }


    }
}
