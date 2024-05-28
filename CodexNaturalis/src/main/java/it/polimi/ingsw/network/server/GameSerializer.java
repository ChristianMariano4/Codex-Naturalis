package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.observer.Listener;

import java.io.*;
import java.util.ArrayList;

public class GameSerializer implements Listener<GameEvent> {
    private static GameSerializer instance;

    public GameSerializer() {
        new File(getJarDir() + File.separator + "savedGames").mkdir();
    }
    private static String getJarDir() {
        String path = Game.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            return new File(path).getParentFile().getPath();
        }  catch (Exception e) {
            return null;
        }
    }

    public static void saveGameState(Game game) throws FileNotFoundException {
        String dir = getJarDir();
        String fileName = null;
        if(dir!=null) {
            fileName = dir + File.separator + "savedGames\\game" + game.getGameId() + ".dir";
            System.out.println("Saving game " + game.getGameId() + " state to: " + fileName);
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                out.writeObject(game);
                System.out.println("Game " + game.getGameId() + " state saved");
            } catch (IOException e) {
                System.err.println("Couldn't save game state");
            }
        } else
        {
            throw new RuntimeException("Couldn't save game state");
        }
    }

    public static ArrayList<Game> loadGamesState(){
        String dir = getJarDir();
        ArrayList<Game> games = new ArrayList<>();
        if(dir!=null) {
            File folder = new File(dir + File.separator + "savedGames");
            File[] listOfFiles = folder.listFiles();
            if(listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                            games.add((Game) in.readObject());
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println("Couldn't load game state");
                        }
                    }
                }
            }
        } else
        {
            throw new RuntimeException("Couldn't load game state");
        }
        return games;
    }

    @Override
    public void update(GameEvent event, Object... args) {
        Game game = (Game) args[0];
        try {
            saveGameState(game);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
