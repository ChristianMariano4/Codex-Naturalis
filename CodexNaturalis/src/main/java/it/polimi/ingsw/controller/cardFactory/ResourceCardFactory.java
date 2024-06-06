package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for creating a list of ResourceCard instances.
 * It extends the CardFactory class with a type parameter of ResourceCard.
 */
public class ResourceCardFactory extends CardFactory<ResourceCard> {

    /**
     * This method creates a list of ResourceCard instances.
     * It reads the card data from a JSON file and uses Gson to parse the JSON data into ResourceCard objects.
     * If an IOException occurs during this process, it throws a CardNotImportedException.
     *
     * @return An ArrayList of ResourceCard instances.
     * @throws CardNotImportedException if there is an error reading the card data from the JSON file.
     */
    public ArrayList<ResourceCard> createCardList() throws CardNotImportedException {
        ResourceCard[] resourceCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("/cardJson/resourceCards.json"))) {
            resourceCardArray = gson.fromJson(reader, ResourceCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(resourceCardArray));
    }
}