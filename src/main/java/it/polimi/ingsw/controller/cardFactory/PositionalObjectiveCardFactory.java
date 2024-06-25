package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class is responsible for creating a list of PositionalObjectiveCard instances.
 * It extends the CardFactory class with a type parameter of ObjectiveCard.
 */
public class PositionalObjectiveCardFactory extends CardFactory<ObjectiveCard> {

    /**
     * This method creates a list of PositionalObjectiveCard instances.
     * It reads the card data from a JSON file and uses Gson to parse the JSON data into PositionalObjectiveCard objects.
     * If an IOException occurs during this process, it throws a CardNotImportedException.
     *
     * @return An ArrayList of ObjectiveCard instances.
     * @throws CardNotImportedException if there is an error reading the card data from the JSON file.
     */
    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        PositionalObjectiveCard[] positionalObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/cardJson/positionalObjectiveCards.json")))) {
            positionalObjectiveCardArray = gson.fromJson(reader, PositionalObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(positionalObjectiveCardArray));
    }
}