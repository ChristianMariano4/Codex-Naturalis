package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.GoldCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class GoldCardFactory {
    public GoldCard[] createGoldCardArray() throws CardNotImportedException {
        GoldCard[] goldCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/goldCards.json")) {
            goldCardArray = gson.fromJson(reader, GoldCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return goldCardArray;

    }
}
