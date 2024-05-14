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

public class TripleObjectiveCardFactory extends CardFactory<ObjectiveCard> {
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