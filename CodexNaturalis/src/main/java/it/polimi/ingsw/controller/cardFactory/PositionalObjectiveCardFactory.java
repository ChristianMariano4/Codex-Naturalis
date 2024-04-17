package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.cardFactory.CardFactory;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class PositionalObjectiveCardFactory extends CardFactory<ObjectiveCard> {
    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        PositionalObjectiveCard[] positionalObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/positionalObjectiveCards.json")) {
            positionalObjectiveCardArray = gson.fromJson(reader, PositionalObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(positionalObjectiveCardArray));
    }
}
