package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.GoldCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class is responsible for creating a list of GoldCard instances.
 * It extends the CardFactory class with a type parameter of GoldCard.
 */
public class GoldCardFactory extends CardFactory<GoldCard> {

    /**
     * This method creates a list of GoldCard instances.
     * It reads the card data from a JSON file and uses Gson to parse the JSON data into GoldCard objects.
     * If an IOException occurs during this process, it throws a CardNotImportedException.
     *
     * @return An ArrayList of GoldCard instances.
     * @throws CardNotImportedException if there is an error reading the card data from the JSON file.
     */
    public ArrayList<GoldCard> createCardList() throws CardNotImportedException {
        GoldCard[] goldCardCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/cardJson/goldCards.json")))) {
            goldCardCardArray = gson.fromJson(reader, GoldCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(goldCardCardArray));
    }
}