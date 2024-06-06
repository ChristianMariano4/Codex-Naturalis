package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.TripleObjectiveCard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for creating a list of TripleObjectiveCard instances.
 * It extends the CardFactory class with a type parameter of ObjectiveCard.
 */
public class TripleObjectiveCardFactory extends CardFactory<ObjectiveCard> {

    /**
     * This method creates a list of TripleObjectiveCard instances.
     * It reads the card data from a JSON file and uses Gson to parse the JSON data into TripleObjectiveCard objects.
     * If an IOException occurs during this process, it throws a CardNotImportedException.
     *
     * @return An ArrayList of ObjectiveCard instances.
     * @throws CardNotImportedException if there is an error reading the card data from the JSON file.
     */
    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        TripleObjectiveCard[] tripleObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("/cardJson/tripleObjectiveCard.json"))) {
            tripleObjectiveCardArray = gson.fromJson(reader, TripleObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(tripleObjectiveCardArray));
    }
}