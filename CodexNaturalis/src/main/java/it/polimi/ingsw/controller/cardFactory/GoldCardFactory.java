package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.GoldCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class GoldCardFactory extends CardFactory<GoldCard> {
    private final String filePath;

    public GoldCardFactory(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<GoldCard> createCardList() throws CardNotImportedException {
        GoldCard[] goldCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader(filePath)) {
            goldCardArray = gson.fromJson(reader, GoldCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(goldCardArray));
    }
}