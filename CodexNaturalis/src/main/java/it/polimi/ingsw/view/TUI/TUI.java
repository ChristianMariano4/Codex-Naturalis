package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.UI;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.*;

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

    public void gameIsStopped()
    {
        System.out.println("You are the ony player left, the game is paused");
    }

    public void showTurnScreen(String playerPlaying, String client, boolean isStopped) {

        if(isStopped)
        {
           gameIsStopped();
        }
        else {
            if (playerPlaying.equals(client)) {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a("""

                                █▄█ █▀█ █░█ █▀█   ▀█▀ █░█ █▀█ █▄░█
                                ░█░ █▄█ █▄█ █▀▄   ░█░ █▄█ █▀▄ █░▀█""").reset());
                System.out.println("\nIt is currently your turn, write playTurn to play.");
            } else {
                new PrintStream(System.out, true, System.console() != null
                        ? System.console().charset()
                        : Charset.defaultCharset())
                        .println(ansi().fg(GREEN).a("""

                                █▄░█ █▀█ ▀█▀   █▄█ █▀█ █░█ █▀█   ▀█▀ █░█ █▀█ █▄░█
                                █░▀█ █▄█ ░█░   ░█░ █▄█ █▄█ █▀▄   ░█░ █▄█ █▀▄ █░▀█""").reset());
                System.out.println("\nIt is currently " + playerPlaying + "'s turn.");
            }
            this.showAllCommands();
            System.out.println("Waiting for input");
        }
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
                .println(ansi().fg(GREEN).a("""

                        █▀▄▀█ ▄▀█ █▀█ █▄▀ █▀▀ █▀█   █▀ █▀▀ █░░ █▀▀ █▀▀ ▀█▀ █ █▀█ █▄░█
                        █░▀░█ █▀█ █▀▄ █░█ ██▄ █▀▄   ▄█ ██▄ █▄▄ ██▄ █▄▄ ░█░ █ █▄█ █░▀█""").reset());
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

    public void showAllExistingGames(List<Game> availableGames) {
        int counter = 0;
        System.out.println("Available Games:");
        for(Game game: availableGames) {
            if(game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                System.out.println(ansi().a("GameID : " + game.getGameId() ));
            } else {
                System.out.println(ansi().a("GameID : " + game.getGameId() + " - ALREADY STARTED"));
            }
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

    public void showPlayerHand(LinkedHashMap<PlayableCard, CardInfo> playerHand) { //create a method in the view to get the playerHand
        //this.clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▄█ █▀█ █░█ █▀█   █░█ ▄▀█ █▄░█ █▀▄
                        ░█░ █▄█ █▄█ █▀▄   █▀█ █▀█ █░▀█ █▄▀""").reset());
            //Print all card information
        for(PlayableCard pc: playerHand.keySet()) {
            showCardInfo(pc, playerHand.get(pc));
        }
    }

    public void showAllPlayers(ArrayList<String> usernames) {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▀█ █░░ ▄▀█ █▄█ █▀▀ █▀█ █▀
                        █▀▀ █▄▄ █▀█ ░█░ ██▄ █▀▄ ▄█""").reset());
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
        System.out.println(player.getUsername() + " points: " + player.getPoints());
    }

    public void scoreboardTitle() {
        //this.clearScreen()
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▀ █▀▀ █▀█ █▀█ █▀▀ █▄▄ █▀█ ▄▀█ █▀█ █▀▄
                        ▄█ █▄▄ █▄█ █▀▄ ██▄ █▄█ █▄█ █▀█ █▀▄ █▄▀""").reset());
    }

    public void showSharedObjectiveCard(HashMap<ObjectiveCard, CardInfo> sharedObjectiveCards) {
        //this.clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▀ █░█ ▄▀█ █▀█ █▀▀ █▀▄   █▀█ █▄▄ ░░█ █▀▀ █▀▀ ▀█▀ █ █░█ █▀▀   █▀▀ ▄▀█ █▀█ █▀▄ █▀
                        ▄█ █▀█ █▀█ █▀▄ ██▄ █▄▀   █▄█ █▄█ █▄█ ██▄ █▄▄ ░█░ █ ▀▄▀ ██▄   █▄▄ █▀█ █▀▄ █▄▀ ▄█""").reset());
        for(ObjectiveCard card: sharedObjectiveCards.keySet()) {
            showCardInfo(card, sharedObjectiveCards.get(card));
        }
    }
    public void showResources(HashMap<Resource, Integer> resources)
    {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                         █▀█ █▀▀ █▀ █▀█ █░█ █▀█ █▀▀ █▀▀ █▀
                         █▀▄ ██▄ ▄█ █▄█ █▄█ █▀▄ █▄▄ ██▄ ▄█""").reset());
        for(Resource resource : resources.keySet())
        {
            System.out.println(resource.toString() + ": "+resources.get(resource));
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
                .println(ansi().fg(GREEN).a("""

                        █▀▀ ▄▀█ █▀▄▀█ █▀▀   █▀▀ █ █▀▀ █░░ █▀▄
                        █▄█ █▀█ █░▀░█ ██▄   █▀░ █ ██▄ █▄▄ █▄▀""").reset());

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
    public void chooseSide()
    {
        System.out.println("Choose card side: 1 - FRONT, 2 - BACK");
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
        for(String player : playersPlacement.keySet())
        {
            System.out.println("            " + player + "-->" + playersPlacement.get(player));
        }
    }

    public void showCardInfo(PlayableCard card, CardInfo cardInfo) {
        System.out.println("CardType: "+ cardInfo.getCardTypeString());
        System.out.println("Id: " +card.getCardId());
        this.asciiCardCreator(card, cardInfo.getCardType().equals(CardType.STARTER));
    }

    private void asciiCardCreator(PlayableCard card, Boolean isStarter) {

        String res;
        Ansi.Color color = WHITE;
        switch (card.getCardColor()) {
            case Resource.FUNGI -> color = RED;
            case Resource.ANIMAL -> color = CYAN;
            case Resource.INSECT -> color = MAGENTA;
            case Resource.PLANT -> color = GREEN;
        }

        if(card.getCurrentSide().equals(Side.FRONT)) {
            StringBuilder output = new StringBuilder();
            System.out.println(card.getCurrentSide());
            if(isStarter) {
                for(int i = 0; i < 7; i++) { //starter
                    switch (i) {
                        case 0:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                output.append("┌───────┬");
                            } else {
                                output.append("┌┬───────");
                            }
                            output.append("───────────");
                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                output.append("┬───────┐\n");
                            } else {
                                output.append("───────┬┐\n");
                            }
                            break;
                        case 1:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.TOPLEFT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│");
                            } else {
                                output.append("││       ");
                            }
                            output.append("  ┌┬───┬┐  ");
                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.TOPRIGHT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 2:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                output.append("│┼──────┘");
                            } else {
                                output.append("││       ");
                            }
                            if(card.getCentralResources().size() > 1){
                                output.append("  ││").append(card.getCentralResources().getFirst().printResourceInfo()).append("││  ");
                            } else {
                                output.append("  ││   ││  ");
                            }

                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                output.append("└──────┼│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 3:
                            if(card.getCentralResources().size() == 1) {
                                output.append("││         ││").append(card.getCentralResources().getFirst().printResourceInfo()).append("││         ││\n");
                            } else if(card.getCentralResources().size() == 3){
                                output.append("││         ││").append(card.getCentralResources().get(1).printResourceInfo()).append("││         ││\n");
                            } else {
                                output.append("││         ││   ││         ││\n");
                            }
                            break;
                        case 4:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                output.append("│┼──────┐");
                            } else {
                                output.append("││       ");
                            }
                            if(card.getCentralResources().size() == 2){
                                output.append("  ││").append(card.getCentralResources().get(1).printResourceInfo()).append("││  ");
                            } else if(card.getCentralResources().size() == 3){
                                output.append("  ││").append(card.getCentralResources().get(2).printResourceInfo()).append("││  ");
                            } else {
                                output.append("  ││   ││  ");
                            }
                            if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                output.append("┌──────┼│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 5:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.BOTTOMLEFT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│");
                            } else {
                                output.append("││       ");
                            }
                            output.append("  └┴───┴┘  ");
                            if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.BOTTOMRIGHT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 6:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                output.append("└───────┴");
                            } else {
                                output.append("└┴───────");
                            }
                            output.append("───────────");
                            if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                output.append("┴───────┘\n");
                            } else {
                                output.append("───────┴┘\n");
                            }
                            break;
                    }
                }
            } else {
                for(int i = 0; i < 7; i++) {
                    switch (i) {
                        case 0:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                output.append("┌───────┬");
                            } else {
                                output.append("┌┬───────");
                            }
                            output.append("───────────");
                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                output.append("┬───────┐\n");
                            } else {
                                output.append("───────┬┐\n");
                            }
                            break;
                        case 1:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.TOPLEFT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│");
                            } else {
                                output.append("││       ");
                            }
                            if(card.getCardId() > 40 && card.getCardId() < 81){
                                if(((GoldCard)card).getGoldPointCondition().equals(GoldPointCondition.NONE)){
                                    output.append("   ").append(card.getPoints()).append(" PTS   ");
                                } else if (((GoldCard)card).getGoldPointCondition().equals(GoldPointCondition.ANGLE)) {
                                    output.append(" 2pt x ANG ");
                                } else {
                                    output.append(" 1pt x ").append(((GoldCard) card).getGoldPointCondition().printResource()).append(" ");
                                }
                            } else {
                                output.append("           ");
                            }
                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.TOPRIGHT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 2:
                            if(card.getAngle(AngleOrientation.TOPLEFT).isPlayable()) {
                                output.append("│┼──────┘");
                            } else {
                                output.append("││       ");
                            }
                            output.append("           ");
                            if(card.getAngle(AngleOrientation.TOPRIGHT).isPlayable()) {
                                output.append("└──────┼│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 3:
                            if(card.getCardId() > 40 && card.getCardId() < 81){
                                if(((GoldCard)card).getRequirementsPairList().size() == 2){
                                    output.append("││           REQ           ││\n");
                                } else {
                                    output.append("││                         ││\n");
                                }
                            } else {
                                output.append("││                         ││\n");
                            }
                            break;

                        case 4:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                output.append("│┼──────┐");
                            } else {
                                output.append("││       ");
                            }
                            if(card.getCardId() > 40 && card.getCardId() < 81){
                                if(((GoldCard)card).getRequirementsPairList().size() == 2){
                                    output.append("  ").append(((GoldCard) card).getRequirementsPairList().get(1).getValue()).append(" x ").append(((GoldCard) card).getRequirementsPairList().get(1).getKey().printResourceInfo()).append("  ");
                                } else {
                                    output.append("    REQ    ");
                                }
                            } else {
                                output.append("           ");
                            }
                            if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                output.append("┌──────┼│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 5:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.BOTTOMLEFT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│");
                            } else {
                                output.append("││       ");
                            }
                            if(card.getCardId() > 40 && card.getCardId() < 81){
                                output.append("  ").append(((GoldCard) card).getRequirementsPairList().getFirst().getValue()).append(" x ").append(((GoldCard) card).getRequirementsPairList().getFirst().getKey().printResourceInfo()).append("  ");

                            } else {
                                output.append("           ");
                            }
                            if(card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                res = card.getAngle(AngleOrientation.BOTTOMRIGHT).getResource().printResourceInfo();
                                output.append("│┤►").append(res).append("◄├│\n");
                            } else {
                                output.append("       ││\n");
                            }
                            break;
                        case 6:
                            if(card.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()) {
                                output.append("└───────┴");
                            } else {
                                output.append("└┴───────");
                            }
                            output.append("───────────");
                            if(/*card.getAngle(AngleOrientation.BOTTOMLEFT).getAngleStatus().equals(AngleStatus.UNLINKED)*/card.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()) {
                                output.append("┴───────┘\n");
                            } else {
                                output.append("───────┴┘\n");
                            }
                            break;
                    }
                }
            }
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(color).a(output.toString()).reset());
        } else { //back
            System.out.println(card.getCurrentSide());
                if (card.getCentralResources().isEmpty()){
                    new PrintStream(System.out, true, System.console() != null
                            ? System.console().charset()
                            : Charset.defaultCharset())
                            .println(ansi().fg(color).a(
                                    "┌───────┬───────────┬───────┐\n" +
                                            "│┤►"+card.getAngle(AngleOrientation.TOPLEFT).getResource().printResourceInfo()+"◄├│           │┤►"+card.getAngle(AngleOrientation.TOPRIGHT).getResource().printResourceInfo()+"◄├│\n" +
                                            "│┼──────┘           └──────┼│\n" +
                                            "││                         ││\n" +
                                            "│┼──────┐           ┌──────┼│\n" +
                                            "│┤►"+card.getAngle(AngleOrientation.BOTTOMLEFT).getResource().printResourceInfo()+"◄├│           │┤►"+card.getAngle(AngleOrientation.BOTTOMRIGHT).getResource().printResourceInfo()+"◄├│\n" +
                                            "└───────┴───────────┴───────┘").reset());
                } else {
                    new PrintStream(System.out, true, System.console() != null
                            ? System.console().charset()
                            : Charset.defaultCharset())
                            .println(ansi().fg(color).a(
                                    "┌───────┬───────────┬───────┐\n" +
                                            "│┤►" + card.getAngle(AngleOrientation.TOPLEFT).getResource().printResourceInfo() + "◄├│           │┤►" + card.getAngle(AngleOrientation.TOPRIGHT).getResource().printResourceInfo() + "◄├│\n" +
                                            "│┼──────┘  ┌┬───┬┐  └──────┼│\n" +
                                            "││         ││" + card.getCentralResources().getFirst().printResourceInfo() + "││         ││\n" +
                                            "│┼──────┐  └┴───┴┘  ┌──────┼│\n" +
                                            "│┤►" + card.getAngle(AngleOrientation.BOTTOMLEFT).getResource().printResourceInfo() + "◄├│           │┤►" + card.getAngle(AngleOrientation.BOTTOMRIGHT).getResource().printResourceInfo() + "◄├│\n" +
                                            "└───────┴───────────┴───────┘").reset());
                }
            }
        }

    public void showCardInfo(ObjectiveCard card, CardInfo cardInfo) {
        if(cardInfo.getCardType().equals(CardType.GOLD) || cardInfo.getCardType().equals(CardType.RESOURCE)) {
            System.out.println("CardType: " + cardInfo.getCardType());
            System.out.println("Resource type: " + cardInfo.getCardColor());
//          System.out.println("Positional condition: " + cardInfo.getPositionalType());
        }
        if(card.getCurrentSide().equals(Side.FRONT)) {
            String cardImage = "";
            Ansi.Color color = WHITE;
            if(cardInfo.getCardType().equals(CardType.POSITIONALOBJECTIVE)) {
                if(cardInfo.getPositionalType().equals(PositionalType.DIAGONAL)) {
                    color = switch (cardInfo.getCardColor()) {
                        case Resource.FUNGI -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │    FUN│        ││
                                    ││ 2 PTS  │  FUN  │        ││
                                    ││        │FUN    │        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield RED;
                        }
                        case Resource.PLANT -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │PLA    │        ││
                                    ││ 2 PTS  │  PLA  │        ││
                                    ││        │    PLA│        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield GREEN;
                        }
                        case Resource.ANIMAL -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │    ANI│        ││
                                    ││ 2 PTS  │  ANI  │        ││
                                    ││        │ANI    │        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield CYAN;
                        }
                        case Resource.INSECT -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │INS    │        ││
                                    ││ 2 PTS  │  INS  │        ││
                                    ││        │    INS│        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield MAGENTA;
                        }
                        default -> color;
                    };
                } else {
                    color = switch (cardInfo.getCardColor()) {
                        case Resource.FUNGI -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │  FUN  │        ││
                                    ││ 3 PTS  │  FUN  │        ││
                                    ││        │    PLA│        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield RED;
                        }
                        case Resource.PLANT -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │  PLA  │        ││
                                    ││ 3 PTS  │  PLA  │        ││
                                    ││        │INS    │        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield GREEN;
                        }
                        case Resource.ANIMAL -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │    FUN│        ││
                                    ││ 3 PTS  │  ANI  │        ││
                                    ││        │  ANI  │        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield CYAN;
                        }
                        case Resource.INSECT -> {
                            cardImage = """
                                    ┌┬─────────────────────────┬┐
                                    ││        ┌───────┐        ││
                                    ││        │ANI    │        ││
                                    ││ 3 PTS  │  INS  │        ││
                                    ││        │  INS  │        ││
                                    ││        └───────┘        ││
                                    └┴─────────────────────────┴┘""";
                            yield MAGENTA;
                        }
                        default -> color;
                    };
                }
            } else if(cardInfo.getCardType().equals(CardType.RESOURCEOBJECTIVE)) {
                if (cardInfo.getCardResource().equals(Resource.MANUSCRIPT) || cardInfo.getCardResource().equals(Resource.INKWELL) || cardInfo.getCardResource().equals(Resource.QUILL)) {
                    cardImage = "┌┬─────────────────────────┬┐\n" +
                            "││          2 PTS          ││\n" +
                            "││     ┌───┐     ┌───┐     ││\n" +
                            "││     │" + cardInfo.getCardResource().printResourceInfo() + "│     │" + cardInfo.getCardResource().printResourceInfo() + "│     ││\n" +
                            "││     └───┘     └───┘     ││\n" +
                            "││                         ││\n" +
                            "└┴─────────────────────────┴┘";
                } else {
                    cardImage = "┌┬─────────────────────────┬┐\n" +
                            "││          2 PTS          ││\n" +
                            "││     ┌───┬┬───┬┬───┐     ││\n" +
                            "││     │" + cardInfo.getCardResource().printResourceInfo() + "││"+ cardInfo.getCardResource().printResourceInfo() +"││" + cardInfo.getCardResource().printResourceInfo() + "│     ││\n" +
                            "││     └───┴┴───┴┴───┘     ││\n" +
                            "││                         ││\n" +
                            "└┴─────────────────────────┴┘";
                }
            } else if(cardInfo.getCardType().equals(CardType.TRIPLEOBJECTIVE)) {
                cardImage = """
                        ┌┬─────────────────────────┬┐
                        ││          3 PTS          ││
                        ││     ┌───┬┬───┬┬───┐     ││
                        ││     │QUI││INK││MAN│     ││
                        ││     └───┴┴───┴┴───┘     ││
                        ││                         ││
                        └┴─────────────────────────┴┘""";
            }
            new PrintStream(System.out, true, System.console() != null
                    ? System.console().charset()
                    : Charset.defaultCharset())
                    .println(ansi().fg(color).a(cardImage).reset());
        }
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
        System.out.println("Choose where to draw the card: \n1 - Resource Card Deck\n2 - Gold Card Deck\n3 - Top Discovered Resource\n4 - Bottom Discovered Resource\n5 - Top Discovered Gold\n6 - Bottom Discovered Gold");
    }
    public void areYouSure()
    {
        System.out.println("Are you sure? Type 'y' or insert Id of another card on the field.");
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
                .println(ansi().fg(GREEN).a("""

                        █▀ ▀█▀ ▄▀█ █▀█ ▀█▀ █▀▀ █▀█   █▀▀ ▄▀█ █▀█ █▀▄
                        ▄█ ░█░ █▀█ █▀▄ ░█░ ██▄ █▀▄   █▄▄ █▀█ █▀▄ █▄▀""").reset());
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
    public void showAllCommands()  {
        System.out.println("""
                All commands:
                   1 - type showPlayers to see all players in the game
                   2 - type myHand to see your hand
                   3 - type showMyField to see your field
                   4 - type playTurn to play your turn
                   5 - type scoreboard to see your or other player points
                   6 - type showOtherField to show another player's field
                   7 - type any card Id to see its information
                   8 - type showSecretObjectiveCard to see your secret objective card
                   9 - type showSharedObjectiveCards to see the shared objective cards
                   10 - type myResources to see you resources
                   11 - type chat to enter the chat
                   12 - type quit to leave the game""");
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
        System.out.println(Thread.currentThread().getClass());
        System.out.println(Thread.currentThread());
    }
    public void requirementsNotMet() {
        System.out.println("The requirements of the selected gold card are not met, try again");
    }
    public void twentyPoints(String username)
    {
        if(username == null)
            System.out.println("Both decks are empty!");
        else
            System.out.println("Player " + username+ "has reached twenty points!");
    }
    public void somethingWentWrong()
    {
        System.out.println("Something went wrong.");
    }
    public void secretObjectiveCardTitle()
    {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▀ █▀▀ █▀▀ █▀█ █▀▀ ▀█▀   █▀█ █▄▄ ░░█ █▀▀ █▀▀ ▀█▀ █ █░█ █▀▀   █▀▀ ▄▀█ █▀█ █▀▄
                        ▄█ ██▄ █▄▄ █▀▄ ██▄ ░█░   █▄█ █▄█ █▄█ ██▄ █▄▄ ░█░ █ ▀▄▀ ██▄   █▄▄ █▀█ █▀▄ █▄▀""").reset());
    }
    public void chooseSecretObjectiveCard()
    {

        System.out.println("Choose one of the two cards: 1 - FIRST, 2 - SECOND.");
    }

    public void playerFiledChoice() {
        System.out.println("Type the username of the player:");
    }


    public void wrongUsername() {
        System.out.println("Wrong username, try again!");
    }

    public void selectNumberOfPlayers() {
        System.out.println("Select the number of players: 2, 3 or 4");
    }

    public void setReady() {
        System.out.println("Press 1 when you are ready to start the game or 0 to quit.");
    }

    public void readyConfirmation() {
        System.out.println("Waiting for other players to be ready...");
    }

    public void printReadyPlayersStatus(ArrayList<Integer> playersInfo) {
        System.out.println("You are ready to start the game. Players ready to start the game: " + playersInfo.get(1) + "/" + playersInfo.getFirst());
    }

    public void printWinner() {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""
                \n
               \s
                        ██╗░░░██╗░░██████╗░░██╗░░░██╗░░░░░█████╗░░██████╗░░███████╗░░░░████████╗░██╗░░██╗░███████╗░░░░██╗░░░░██╗░██╗░███╗░░░██╗░███╗░░░██╗░███████╗░██████╗░
                        ╚██╗░██╔╝░██╔═══██╗░██║░░░██║░░░░██╔══██╗░██╔══██╗░██╔════╝░░░░╚══██╔══╝░██║░░██║░██╔════╝░░░░██║░░░░██║░██║░████╗░░██║░████╗░░██║░██╔════╝░██╔══██╗
                         ╚████╔╝░░██║░░░██║░██║░░░██║░░░░███████║░██████╔╝░█████╗░░░░░░░░░██║░░░░███████║░█████╗░░░░░░██║░█╗░██║░██║░██╔██╗░██║░██╔██╗░██║░█████╗░░░██████╔╝
                          ╚██╔╝░░░██║░░░██║░██║░░░██║░░░░██╔══██║░██╔══██╗░██╔══╝░░░░░░░░░██║░░░░██╔══██║░██╔══╝░░░░░░██║███╗██║░██║░██║╚██╗██║░██║╚██╗██║░██╔══╝░░░██╔══██╗
                           ██║░░░░╚██████╔╝░╚██████╔╝░░░░██║░░██║░██║░░██║░███████╗░░░░░░░██║░░░░██║░░██║░███████╗░░░░╚███╔███╔╝░██║░██║░╚████║░██║░╚████║░███████╗░██║░░██║
                           ╚═╝░░░░░╚═════╝░░░╚═════╝░░░░░╚═╝░░╚═╝░╚═╝░░╚═╝░╚══════╝░░░░░░░╚═╝░░░░╚═╝░░╚═╝░╚══════╝░░░░░╚══╝╚══╝░░╚═╝░╚═╝░░╚═══╝░╚═╝░░╚═══╝░╚══════╝░╚═╝░░╚═╝     \s
               \s""").reset());
    }

    public void gameEndDisconnection() {
        System.out.println("You were the only player left in the game. The game has ended.\n" +
                "Press any key to exit the game.");
    }

    public void showChat(String chatHistory)
    {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset())
                .println(ansi().fg(GREEN).a("""

                        █▀▀ █░█ ▄▀█ ▀█▀
                        █▄▄ █▀█ █▀█ ░█░""").reset());
        System.out.println(chatHistory);
    }

    public void showNewMessage(String message) {
        System.out.println(message);

    }

    public void showGameQuitScreen() {
        System.out.println("You have quit the game.");
    }
}
