package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for creating a list of StarterCard instances.
 * It extends the CardFactory class with a type parameter of StarterCard.
 */
public class StarterCardFactory extends CardFactory<StarterCard> {

    /**
     * This method creates a list of StarterCard instances.
     * It reads the card data from a JSON file and uses Gson to parse the JSON data into StarterCard objects.
     * If an IOException occurs during this process, it throws a CardNotImportedException.
     *
     * @return An ArrayList of StarterCard instances.
     * @throws CardNotImportedException if there is an error reading the card data from the JSON file.
     */
    public ArrayList<StarterCard> createCardList() throws CardNotImportedException {
        StarterCard[] starterCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("/cardJson/starterCards.json"))) {
            starterCardArray = gson.fromJson(reader, StarterCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(starterCardArray));
    }
}