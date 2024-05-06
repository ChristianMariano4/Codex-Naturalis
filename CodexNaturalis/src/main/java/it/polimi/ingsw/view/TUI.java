package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class TUI extends UI{

    public TUI() {
        init();
    }

    @Override
    public void init() {
        AnsiConsole.systemInstall();
    }

    public void showGameTitle() {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a(""" 
                        ░█████╗░░█████╗░██████╗░███████╗██╗░░██╗  ███╗░░██╗░█████╗░████████╗██╗░░░██╗██████╗░░█████╗░██╗░░░░░██╗░██████╗
                        ██╔══██╗██╔══██╗██╔══██╗██╔════╝╚██╗██╔╝  ████╗░██║██╔══██╗╚══██╔══╝██║░░░██║██╔══██╗██╔══██╗██║░░░░░██║██╔════╝
                        ██║░░╚═╝██║░░██║██║░░██║█████╗░░░╚███╔╝░  ██╔██╗██║███████║░░░██║░░░██║░░░██║██████╔╝███████║██║░░░░░██║╚█████╗░
                        ██║░░██╗██║░░██║██║░░██║██╔══╝░░░██╔██╗░  ██║╚████║██╔══██║░░░██║░░░██║░░░██║██╔══██╗██╔══██║██║░░░░░██║░╚═══██╗
                        ╚█████╔╝╚█████╔╝██████╔╝███████╗██╔╝╚██╗  ██║░╚███║██║░░██║░░░██║░░░╚██████╔╝██║░░██║██║░░██║███████╗██║██████╔╝
                        ░╚════╝░░╚════╝░╚═════╝░╚══════╝╚═╝░░╚═╝  ╚═╝░░╚══╝╚═╝░░╚═╝░░░╚═╝░░░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝╚═╝╚═════╝░                                                                                                                                                                                                  \s
                """).reset());
        //ℭ𝔬𝔡𝔢𝔵 𝔑𝔞𝔱𝔲𝔯𝔞𝔩𝔦𝔰
    }

    public void showAllExistingGames(List<Integer> availableGames) {
        int counter = 0;
        System.out.println("Available Games:");
        for(Integer game: availableGames) {
            counter++;
            System.out.println("GameID n°" + counter + ": " + game);
        }
        System.out.print("Enter the game id you want to join: ");
    }

    public void createOrJoinAGame() {
        System.out.println("Do you want to create a game or join an existing one?");
        System.out.println("1. Create a game\n2. Join a game");
    }

    public void setUsername() {
        System.out.println("Insert your username: ");
    }

    public void showPlayerHand() { //create a method in the view to get the playerHand
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""
                        |-----------------|
                        |       ---       |
                        |      | @ |    __|
                        |       ---    |  |
                        |--------------|--|
                        """).reset());
    }

    public void showOtherPlayerTableTop(Player player) {

    }

    public void showDiscoveredCards() {

    }

    public void showPoints(Player player) {

    }

    public void showSharedObjectiveCard() {

    }

    public void showHiddenObjectiveCards() {

    }

    public void aPlayerJoinedTheGame() {

    }

    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {}
    }

}
