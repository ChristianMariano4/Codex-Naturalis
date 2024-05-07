package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

    public void showGameStarted() {
        this.clearScreen();
        this.showGameTitle();
        //this.showAllPlayers();
    }

    public void showMainChoices() {
        System.out.println( "q: quit \n" +
                            "h: help\n" +
                            "showplayers: show all players in the game\n" +
                            "myhand: show your hand");
    }

    public void showMainScreen() {
        this.clearScreen();
        this.showGameTitle();
        this.showMainChoices();
    }

    public void redirectByPlayerChoice() {
        this.showPlayerHand();
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
        this.clearScreen();
        int test = 0;
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▄█ █▀█ █░█ █▀█   █░█ ▄▀█ █▄░█ █▀▄\n" +
                        "░█░ █▄█ █▄█ █▀▄   █▀█ █▀█ █░▀█ █▄▀").reset());
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(GREEN).a(
                              "┌──────┬───────────┬──────┐    ┌──────┬───────────┬──────┐     ┌──────┬───────────┬──────┐\n" +
                                    "│┤►@@◄├│           │┤►@@◄├│    │┤►@@◄├│           │┤►@@◄├│     │┤►@@◄├│           │┤►@@◄├│\n" +
                                    "│┼─────┘  ┌┬───┬┐  └─────┼│    │┼─────┘  ┌┬───┬┐  └─────┼│     │┼─────┘  ┌┬───┬┐  └─────┼│\n" +
                                    "││        ││ "+test+" ││        ││    ││        ││ "+test+" ││        ││     ││        ││ "+test+" ││        ││\n" +
                                    "││        └┴───┴┘  ┌─────┼│    ││        └┴───┴┘  ┌─────┼│     ││        └┴───┴┘  ┌─────┼│\n" +
                                    "││                 │┤►@@◄├│    ││                 │┤►@@◄├│     ││                 │┤►@@◄├│\n" +
                                    "└┴─────────────────┴──────┘    └┴─────────────────┴──────┘     └┴─────────────────┴──────┘").reset());
            //Print all card information
                System.out.println("card "+test+"                          card "+test+"                          card "+test);
                System.out.println("descrizione                     descrizione                     descrizione");

    }
    public void showAllPlayers(ArrayList<String> usernames) {
        int playerCounter = 1;
        for(String username : usernames) {
            System.out.println("Player " + playerCounter + ": " + username);
            playerCounter++;
        }

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

    public void showPlayerTableTop() {}

    public void showEndGameScreen() {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a(""" 
                        ███████  ██████  ██████  ██████  ███████ ██████   ██████   █████  ██████  ██████ \s
                        ██      ██      ██    ██ ██   ██ ██      ██   ██ ██    ██ ██   ██ ██   ██ ██   ██\s
                        ███████ ██      ██    ██ ██████  █████   ██████  ██    ██ ███████ ██████  ██   ██\s
                             ██ ██      ██    ██ ██   ██ ██      ██   ██ ██    ██ ██   ██ ██   ██ ██   ██\s
                        ███████  ██████  ██████  ██   ██ ███████ ██████   ██████  ██   ██ ██   ██ ██████ \s
                                                                                                         \s
                """).reset());
        //list of the players based on their points
    }

    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println("SCREEN CLEAN");
        }
    }
    public void showAllCommands()
    {
        System.out.println("Write all possible commands here"); //TODO: all commands
    }
    public void commandNotFound()
    {
        System.out.println("Invalid command");
    }

}
