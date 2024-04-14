package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class StarterCardFactory {
    public ArrayList<StarterCard> createStarterCardList() throws CardNotImportedException {
        StarterCard[] starterCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/starterCards.json")) {
            starterCardArray = gson.fromJson(reader, StarterCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(starterCardArray));

    }
}
