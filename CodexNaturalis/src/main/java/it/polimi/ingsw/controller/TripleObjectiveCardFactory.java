package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.TripleObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class TripleObjectiveCardFactory {
    public TripleObjectiveCard createTripleObjectiveCard() throws CardNotImportedException {
        TripleObjectiveCard tripleObjectiveCard;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/tripleObjectiveCard.json")) {
            tripleObjectiveCard = gson.fromJson(reader, TripleObjectiveCard.class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return tripleObjectiveCard;
    }
}
