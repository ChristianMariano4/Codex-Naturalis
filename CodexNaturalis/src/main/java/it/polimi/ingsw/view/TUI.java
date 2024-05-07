package it.polimi.ingsw.view;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
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

    public void showPlayerHand(HashMap<PlayableCard, CardInfo> playerHand) { //create a method in the view to get the playerHand
        this.clearScreen();
        int test = 0;
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▄█ █▀█ █░█ █▀█   █░█ ▄▀█ █▄░█ █▀▄\n" +
                        "░█░ █▄█ █▄█ █▀▄   █▀█ █▀█ █░▀█ █▄▀").reset());
            //Print all card information
                for(PlayableCard pc: playerHand.keySet()) {
                    new PrintStream(System.out, true, System.console() != null
                            ? System.console().charset()
                            : Charset.defaultCharset())
                            .println(ansi().fg(GREEN).a(
                                    "┌──────┬───────────┬──────┐\n" +
                                            "│┤►@@◄├│           │┤►@@◄├│\n" +
                                            "│┼─────┘  ┌┬───┬┐  └─────┼│\n" +
                                            "││        ││ "+pc.getCardId()+" ││        ││\n" +
                                            "││        └┴───┴┘  ┌─────┼│\n" +
                                            "││                 │┤►@@◄├│\n" +
                                            "└┴─────────────────┴──────┘").reset());
                    System.out.println("CardType: "+playerHand.get(pc).getCardType());
                    System.out.println("Requirements:");
                    for(Resource rs: playerHand.get(pc).getRequirements()) {
                        System.out.println("    " + rs.toString());
                    }
                    System.out.println("GoldPointCondition: "+playerHand.get(pc).getGoldPointCondition());
                    System.out.println("GoldPointCondition: "+playerHand.get(pc).getGoldPointCondition());
                }
    }
    public void showAllPlayers(ArrayList<String> usernames) {
        int playerCounter = 1;
        for(String username : usernames) {
            System.out.println("Player " + playerCounter + ": " + username);
            playerCounter++;
        }

    }

    public void showDiscoveredCards(HashMap<GoldCard, CardInfo> discoveredGoldCards, HashMap<ResourceCard, CardInfo> discoveredResourceCards) {
        System.out.println("Discovered GoldCards:");
        for(GoldCard card: discoveredGoldCards.keySet()) {
            System.out.println(card.getCardId());

            System.out.println("CardType: "+discoveredGoldCards.get(card).getCardType());
            System.out.println("Requirements:");
            for(Resource rs: discoveredGoldCards.get(card).getRequirements()) {
                System.out.println("    " + rs.toString());
            }
            System.out.println("GoldPointCondition: "+discoveredGoldCards.get(card).getGoldPointCondition());
            System.out.println("GoldPointCondition: "+discoveredGoldCards.get(card).getGoldPointCondition());
        }
        System.out.println("Discovered ResourceCards:");
        for(ResourceCard card: discoveredResourceCards.keySet()) {
            System.out.println(card.getCardId());

            System.out.println("CardType: "+discoveredResourceCards.get(card).getCardType());
            System.out.println("Requirements:");
            for(Resource rs: discoveredResourceCards.get(card).getRequirements()) {
                System.out.println("    " + rs.toString());
            }
            System.out.println("GoldPointCondition: "+discoveredResourceCards.get(card).getGoldPointCondition());
            System.out.println("GoldPointCondition: "+discoveredResourceCards.get(card).getGoldPointCondition());
        }
    }

    public void showPoints(Player player) {
        System.out.println(player.getUsername() + "points: " + player.getPoints());
    }

    public void showSharedObjectiveCard(HashMap<ObjectiveCard, CardInfo> sharedObjectiveCards) {
        for(ObjectiveCard card: sharedObjectiveCards.keySet()) {
            System.out.println("CardId:" + card.getCardId());
            System.out.println("Card Type:" + sharedObjectiveCards.get(card).getCardType());
            System.out.println("Positional Type:" + sharedObjectiveCards.get(card).getPositionalType());
            System.out.println("Card Resource:" + sharedObjectiveCards.get(card).getCardResource());
        }
    }
    public void showHiddenObjectiveCards(ObjectiveCard secretObjectiveCard, CardInfo secretCardInfo) {
        System.out.println("CardId:" + secretObjectiveCard.getCardId());
        System.out.println("Card Type:" + secretCardInfo.getCardType());
        System.out.println("Positional Type:" + secretCardInfo.getPositionalType());
        System.out.println("Card Resource:" + secretCardInfo.getCardResource());
    }

    public void aPlayerJoinedTheGame(String username) {
        System.out.println(username + "joined the game.");
    }

    public void showPlayerField(int[][] pField) {

        pField[37][37] = 1;
        pField[38][36] = 1;

        for(int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
            for(int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                if(pField[i][j] == 0) {
                    System.out.print(ansi().a("   "));
                } else {
                    System.out.print(ansi().a("|"+ pField[i][j] + "|"));
                }
            }
            System.out.print("\n");
        }
    }

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

    public void clearScreen() { //TODO: non funziona :(
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
