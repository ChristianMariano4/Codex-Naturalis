package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.UI;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import javax.swing.text.html.StyleSheet;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class TUI extends UI {

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

    public void showTurnScreen(String playerPlaying, String client) {
        this.clearScreen();

        if(playerPlaying.equals(client)) {
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(GREEN).a("\n" +
                            "█▄█ █▀█ █░█ █▀█   ▀█▀ █░█ █▀█ █▄░█\n" +
                            "░█░ █▄█ █▄█ █▀▄   ░█░ █▄█ █▀▄ █░▀█").reset());
            System.out.println("\nIt is currently your turn, write playTurn to play.");
        } else {
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(GREEN).a("\n" +
                            "█▄░█ █▀█ ▀█▀   █▄█ █▀█ █░█ █▀█   ▀█▀ █░█ █▀█ █▄░█\n" +
                            "█░▀█ █▄█ ░█░   ░█░ █▄█ █▄█ █▀▄   ░█░ █▄█ █▀▄ █░▀█").reset());
            System.out.println("It is currently "+ playerPlaying+"'s turn.");
        }
        this.showAllCommands();
        System.out.println("Waiting for input");
    }

    public void showMainScreen() {
        this.clearScreen();
        this.showGameTitle();
    }
    public void waitingForMarkerTurn()
    {
        System.out.println("Wait for your turn to select the marker");
    }
    public void showAvailableMarkers(ArrayList<Marker> markers)
    {
        this.clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀▄▀█ ▄▀█ █▀█ █▄▀ █▀▀ █▀█   █▀ █▀▀ █░░ █▀▀ █▀▀ ▀█▀ █ █▀█ █▄░█\n" +
                        "█░▀░█ █▀█ █▀▄ █░█ ██▄ █▀▄   ▄█ ██▄ █▄▄ ██▄ █▄▄ ░█░ █ █▄█ █░▀█").reset());
        int counter = 1;
        System.out.print("Select your marker: ");
        for(Marker marker : markers)
        {
            System.out.print(counter + " - " + marker);
            if(counter != markers.size())
            {
                System.out.print(", ");
            }
            counter += 1;
        }
        System.out.print(".\n");
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
    }

    public void showAllExistingGames(List<Integer> availableGames) {
        int counter = 0;
        System.out.println("Available Games:");
        for(Integer game: availableGames) {
            counter++;
            System.out.println(ansi().a("GameID n°" + counter + ": " + game));
        }
        System.out.print("Enter the game id you want to join: ");
    }

    public void createOrJoinAGame() {
        System.out.println("\nDo you want to create a game or join an existing one?");
        System.out.println("1 - Create a game\n2 - Join a game\n0 - quit");
    }

    public void setUsername() {
        System.out.println("Insert your username: ");
    }

    public void showPlayerHand(HashMap<PlayableCard, CardInfo> playerHand) { //create a method in the view to get the playerHand
        //this.clearScreen();
        int test = 0;
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▄█ █▀█ █░█ █▀█   █░█ ▄▀█ █▄░█ █▀▄\n" +
                        "░█░ █▄█ █▄█ █▀▄   █▀█ █▀█ █░▀█ █▄▀").reset());
            //Print all card information
        for(PlayableCard pc: playerHand.keySet()) {
            showCardInfo(pc, playerHand.get(pc));
        }
    }

    public void showAllPlayers(ArrayList<String> usernames) {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀█ █░░ ▄▀█ █▄█ █▀▀ █▀█ █▀\n" +
                        "█▀▀ █▄▄ █▀█ ░█░ ██▄ █▀▄ ▄█").reset());
        int playerCounter = 1;
        for(String username : usernames) {
            System.out.println("Player " + playerCounter + ": " + username);
            playerCounter++;
        }

    }

    public void showDiscoveredCards(HashMap<GoldCard, CardInfo> discoveredGoldCards, HashMap<ResourceCard, CardInfo> discoveredResourceCards) {
        System.out.println("Discovered GoldCards:");
        for(GoldCard card: discoveredGoldCards.keySet()) {
            showCardInfo(card, discoveredGoldCards.get(card));
        }
        System.out.println("Discovered ResourceCards:");
        for(ResourceCard card: discoveredResourceCards.keySet()) {
            showCardInfo(card, discoveredResourceCards.get(card));
        }
    }

    public void showPoints(Player player) {
        System.out.println(player.getUsername() + "points: " + player.getPoints());
    }

    public void scoreboardTitle() {
        //this.clearScreen()
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀ █▀▀ █▀█ █▀█ █▀▀ █▄▄ █▀█ ▄▀█ █▀█ █▀▄\n" +
                        "▄█ █▄▄ █▄█ █▀▄ ██▄ █▄█ █▄█ █▀█ █▀▄ █▄▀").reset());
    }

    public void showSharedObjectiveCard(HashMap<ObjectiveCard, CardInfo> sharedObjectiveCards) {
        //this.clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀ █░█ ▄▀█ █▀█ █▀▀ █▀▄   █▀█ █▄▄ ░░█ █▀▀ █▀▀ ▀█▀ █ █░█ █▀▀   █▀▀ ▄▀█ █▀█ █▀▄ █▀\n" +
                        "▄█ █▀█ █▀█ █▀▄ ██▄ █▄▀   █▄█ █▄█ █▄█ ██▄ █▄▄ ░█░ █ ▀▄▀ ██▄   █▄▄ █▀█ █▀▄ █▄▀ ▄█").reset());
        for(ObjectiveCard card: sharedObjectiveCards.keySet()) {
            showCardInfo(card, sharedObjectiveCards.get(card));
        }
    }

    public void aPlayerJoinedTheGame(String username) {
        System.out.println(username + "joined the game.");
    }

    public void showPlayerField(int[][] pField) {
        //this.clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀▀ ▄▀█ █▀▄▀█ █▀▀   █▀▀ █ █▀▀ █░░ █▀▄\n" +
                        "█▄█ █▀█ █░▀░█ ██▄   █▀░ █ ██▄ █▄▄ █▄▀").reset());

        int minRow = DEFAULT_MATRIX_SIZE;
        int maxRow = 0;
        int minColum = DEFAULT_MATRIX_SIZE;
        int maxColum = 0;

        for(int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
            for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                if(pField[i][j] > 0) {
                    if(minRow > i) {minRow = i;}
                    if(maxRow < i) {maxRow = i;}
                    if(minColum > j) {minColum = j;}
                    if(maxColum < j) {maxColum = j;}
                }
            }
        }
        for(int i = minRow-1; i < maxRow+1; i++) {
            for(int j = minColum-1; j < maxColum+1; j++) {
                if(pField[i][j] == 0) {
                    System.out.print(ansi().a("   "));
                } else {
                    System.out.print(ansi().a("|"+ pField[i][j] + "|"));
                }
            }
            System.out.print("\n");
        }
    }

    public void showEndGameScreen(LinkedHashMap<String, Integer> playersPlacement) {
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
        for(String username: playersPlacement.keySet()) {
            int check = 0;
            for(String temp: playersPlacement.keySet()) {
                if(playersPlacement.get(username) < playersPlacement.get(temp)) {
                    check++;
                }
            }
            if(check == 0) {
                System.out.println("            " + username + "-->" + playersPlacement.get(username));
                playersPlacement.remove(username);
            }
        }
    }
    public void showCardInfo(PlayableCard card, CardInfo cardInfo) {
        this.asciiCardCreator(card);
        System.out.println("Id: " +card.getCardId());
        System.out.println("CardType: "+ cardInfo.getCardType());

        if(cardInfo.getCardType().equals(CardType.GOLD)) {
            System.out.println("Requirements:");
            for(Resource rs: cardInfo.getRequirements()) {
                System.out.println("    " + rs.toString());
            }
            System.out.println("GoldPointCondition: "+ cardInfo.getGoldPointCondition());
            System.out.println("GoldPointCondition: "+ cardInfo.getGoldPointCondition());
        }
    }
    private void asciiCardCreator(PlayableCard card) {
    //TODO: extends the method for all unlinked angles during the game
        Ansi.Color color = WHITE;
        switch (card.getCardColor()) {
            case Resource.FUNGI -> color = RED;
            case Resource.ANIMAL -> color = CYAN;
            case Resource.INSECT -> color = MAGENTA;
            case Resource.PLANT -> color = GREEN;
        }

        if(card.getCurrentSide().equals(Side.FRONT)) {

            //TODO: startercard
            System.out.println(card.getCurrentSide() + ":");
            StringBuilder output = new StringBuilder();
            for(int i = 0; i < 7; i++) {
                switch (i) {
                    case 0:
                        if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                            output.append("┌──────┬");
                        } else {
                            output.append("┌┬──────");
                        }
                        output.append("───────────");
                        if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                            output.append("┬──────┐\n");
                        } else {
                            output.append("──────┬┐\n");
                        }
                        break;
                    case 1:
                        if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                            output.append("│┤►@@◄├│");
                        } else {
                            output.append("││      ");
                        }
                        output.append("           ");
                        if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                            output.append("│┤►@@◄├│\n");
                        } else {
                            output.append("      ││\n");
                        }
                        break;
                    case 2:
                        if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                            output.append("│┼─────┘");
                        } else {
                            output.append("││      ");
                        }
                        output.append("  ┌┬───┬┐  ");
                        if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                            output.append("└─────┼│\n");
                        } else {
                            output.append("      ││\n");
                        }
                        break;
                    case 3:
                        if(card.getCardId() > 9) {
                            output.append("││        ││").append(card.getCardId()).append(" ││        ││\n");
                        } else {
                            output.append("││        ││ ").append(card.getCardId()).append(" ││        ││\n");
                        }
                        break;
                    case 4:
                        if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                            output.append("│┼─────┐");
                        } else {
                            output.append("││      ");
                        }
                        output.append("  └┴───┴┘  ");
                        if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                            output.append("┌─────┼│\n");
                        } else {
                            output.append("      ││\n");
                        }
                        break;
                    case 5:
                        if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                            output.append("│┤►@@◄├│");
                        } else {
                            output.append("││      ");
                        }
                        output.append("           ");
                        if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                            output.append("│┤►@@◄├│\n");
                        } else {
                            output.append("      ││\n");
                        }
                        break;
                    case 6:
                        if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                            output.append("└──────┴");
                        } else {
                            output.append("└┴──────");
                        }
                        output.append("───────────");
                        if(/*card.getAngle(AngleOrientation.BOTTOMLEFT).getAngleStatus().equals(AngleStatus.UNLINKED)*/card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                            output.append("┴──────┘\n");
                        } else {
                            output.append("──────┴┘\n");
                        }
                        break;
                }
            }
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(color).a(output.toString()).reset());
        } else {
            System.out.println(card.getCurrentSide() + ":");
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(color).a("┌──────┬───────────┬──────┐\n" +
                            "│┤►&&◄├│           │┤►&&◄├│\n" +
                            "│┼─────┘  ┌─────┐  └─────┼│\n" +
                            "││        │  "+card.getCardId()+"  │        ││\n" +
                            "│┼─────┐  └─────┘  ┌─────┼│\n" +
                            "│┤►&&◄├│           │┤►&&◄├│\n" +
                            "└──────┴───────────┴──────┘").reset());
        }
    }
    public void showCardInfo(ObjectiveCard card, CardInfo cardInfo) {
        //TODO: custom graphic for objective cards

        if(card.getCurrentSide().equals(Side.FRONT)) {
            String cardImage = "";
            Ansi.Color color = WHITE;
            if(cardInfo.getCardType().equals(CardType.POSITIONALOBJECTIVE)) {
                if(cardInfo.getPositionalType().equals(PositionalType.DIAGONAL)) {
                    color = switch (cardInfo.getCardColor()) {
                        case Resource.FUNGI -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield RED;
                        }
                        case Resource.PLANT -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield GREEN;
                        }
                        case Resource.ANIMAL -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield CYAN;
                        }
                        case Resource.INSECT -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield MAGENTA;
                        }
                        default -> color;
                    };
                } else {
                    color = switch (cardInfo.getCardColor()) {
                        case Resource.FUNGI -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield RED;
                        }
                        case Resource.PLANT -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield GREEN;
                        }
                        case Resource.ANIMAL -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │    @  │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield CYAN;
                        }
                        case Resource.INSECT -> {
                            cardImage = "┌┬───────┬───────┬───────┬┐\n" +
                                    "││       ├───────┤       ││\n" +
                                    "││       │  @    │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       │   @   │       ││\n" +
                                    "││       ├───────┤       ││\n" +
                                    "└┴───────┴───────┴───────┴┘";
                            yield MAGENTA;
                        }
                        default -> color;
                    };
                }
            } else if(cardInfo.getCardType().equals(CardType.RESOURCEOBJECTIVE)) {
                cardImage = "┌┬───────────────────────┬┐\n" +
                        "││                       ││\n" +
                        "││    ┌───┐     ┌───┐    ││\n" +
                        "││    │ @ │     │ & │    ││\n" +
                        "││    └───┘     └───┘    ││\n" +
                        "││                       ││\n" +
                        "└┴───────────────────────┴┘";
            } else if(cardInfo.getCardType().equals(CardType.TRIPLEOBJECTIVE)) {
                cardImage = "┌┬───────────────────────┬┐\n" +
                        "││                       ││\n" +
                        "││    ┌───┬┬───┬┬───┐    ││\n" +
                        "││    │ @ ││ % ││ & │    ││\n" +
                        "││    └───┴┴───┴┴───┘    ││\n" +
                        "││                       ││\n" +
                        "└┴───────────────────────┴┘";
            }
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(color).a(cardImage).reset());
        }

        System.out.println("CardType: " + cardInfo.getCardType());
        System.out.println("Resource type: " + cardInfo.getCardColor());
        System.out.println("Positional condition: " + cardInfo.getPositionalType());
    }

    public void showCardInfo(StarterCard card, CardInfo cardInfo) {
        //TODO: custom graphic for objective cards
        if(card.getCardId() <= 9) {
            if(card.getCurrentSide().equals(Side.FRONT)) {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a(
                                "┌──────┬───────────┬──────┐\n" +
                                        "│┤►@@◄├│           │┤►@@◄├│\n" +
                                        "│┼─────┘  ┌─────┐  └─────┼│\n" +
                                        "││        │  "+card.getCardId()+"  │        ││\n" +
                                        "│┼─────┐  └─────┘  ┌─────┼│\n" +
                                        "│┤►@@◄├│           │┤►@@◄├│\n" +
                                        "└──────┴───────────┴──────┘").reset());
            } else {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a(
                                "┌┬───────────────────────┬┐\n" +
                                        "││        ┌┬───┬┐        ││\n" +
                                        "││        ││   ││        ││\n" +
                                        "││        ││ "+card.getCardId()+" ││        ││\n" +
                                        "││        ││   ││        ││\n" +
                                        "││        └┴───┴┘        ││\n" +
                                        "└┴───────────────────────┴┘").reset());
            }
        } else {
            if(card.getCurrentSide().equals(Side.FRONT)) {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a(
                                "┌──────┬───────────┬──────┐\n" +
                                        "│┤►@@◄├│           │┤►@@◄├│\n" +
                                        "│┼─────┘  ┌─────┐  └─────┼│\n" +
                                        "││        │ "+card.getCardId()+"  │        ││\n" +
                                        "│┼─────┐  └─────┘  ┌─────┼│\n" +
                                        "│┤►@@◄├│           │┤►@@◄├│\n" +
                                        "└──────┴───────────┴──────┘").reset());
            } else {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a(
                                "┌┬───────────────────────┬┐\n" +
                                        "││        ┌┬───┬┐        ││\n" +
                                        "││        ││   ││        ││\n" +
                                        "││        ││"+card.getCardId()+" ││        ││\n" +
                                        "││        ││   ││        ││\n" +
                                        "││        └┴───┴┘        ││\n" +
                                        "└┴───────────────────────┴┘").reset());
            }
        }

        System.out.println("CardType: " + cardInfo.getCardType());
        System.out.println("Resource type: " + cardInfo.getCardColor());
        System.out.println("Positional condition: " + cardInfo.getPositionalType());
    }
    public void chooseCardToPlay()
    {
        System.out.println("Insert the ID of the card you want to play");
    }
    public void chooseWhereToPlay()
    {
        System.out.println("Insert the ID of the card where you want to place your card");
    }
    public void chooseAngle()
    {
        System.out.println("Choose the angle where you want to play the card: 1 - TOPRIGHT, 2 - TOPLEFT, 3 - BOTTOMRIGHT, 4 - BOTTOMLEFT, 0 - cancel");
    }
    public void notEnoughPlayers()
    {
        System.out.println("Not enough players, minimum amount of players is 2.");
    }
    public void drawCard()
    {
        System.out.println("Choose where to draw the card: 1 - Resource Card Deck, 2 - Gold Card Deck, 3 - Left Discovered Resource, 4 - Right Discovered Resource, 5 - Left Discovered Gold, 6 - Right Discovered Gold, 0 - cancel");
    }
    public void waitingForOthers()
    {
        System.out.println("Waiting for other players to choose their marker");
    }

    public void waitingForGameBegin()
    {
        System.out.println("Waiting for the game to begin...");
    }
    public void finalRound()
    {
        System.out.println("Final round begins now!");
    }
    public void chooseStarterCardSide(StarterCard cardFront, StarterCard cardBack, CardInfo cardFrontInfo, CardInfo cardBackInfo)
    {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("\n" +
                        "█▀ ▀█▀ ▄▀█ █▀█ ▀█▀ █▀▀ █▀█   █▀▀ ▄▀█ █▀█ █▀▄\n" +
                        "▄█ ░█░ █▀█ █▀▄ ░█░ ██▄ █▀▄   █▄▄ █▀█ █▀▄ █▄▀").reset());
        showCardInfo(cardFront, cardFrontInfo);
        showCardInfo(cardBack, cardBackInfo);
        System.out.println("\nChoose card Side: 1 - FRONT, 2 - BACK");
    }
    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println("SCREEN CLEAN");
        }
    }
    public void showAllCommands()  { //TODO: all commands
        System.out.println("All commands:\n" +
                "   1. type showPlayers to see all players in the game\n" +
                "   2. type myHand to see your hand\n" +
                "   3. type showMyField to see your filed\n" +
                "   4. type playTurn to play your turn\n" +
                "   5. type showPoints to see your or other player points\n");
    }
    public void commandNotFound() {
        System.out.println(ansi().fg(RED).a("Invalid command"));
    }
    public void notYourTurn(String currentPlayer) {
        System.out.println("Wait until it's your turn to play, currently it's " + currentPlayer +"'s turn.");
    }

    public void invalidInput()
    {
        System.out.println("Invalid input, try again");
    }
    public void twentyPoints(String username)
    {
        System.out.println("Player " + username+ "has reached twenty points!");
    }
    public void somethingWentWrong()
    {
        System.out.println("Something went wrong.");
    }
    public void chooseSecretObjectiveCard()
    {
        System.out.println("Choose one of the two cards: 1 - first, 2 - second.");
    }

    public void playerFiledChoice() {
        System.out.println("Type the username of the player:");
    }


}
