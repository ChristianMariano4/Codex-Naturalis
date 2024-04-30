package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.cardFactory.CardFactory;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.TripleObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class TripleObjectiveCardFactory extends CardFactory<ObjectiveCard> {
    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        TripleObjectiveCard[] tripleObjectiveCard;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/src/main/resources/tripleObjectiveCard.json")) {
            tripleObjectiveCard = gson.fromJson(reader, TripleObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(tripleObjectiveCard));
    }
}
