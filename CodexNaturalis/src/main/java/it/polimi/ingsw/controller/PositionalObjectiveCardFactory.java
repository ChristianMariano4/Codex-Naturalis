package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class PositionalObjectiveCardFactory {
    public PositionalObjectiveCard[] positionalObjectiveCardArray() throws CardNotImportedException {
        PositionalObjectiveCard[] positionalObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/positionalObjectiveCards.json")) {
            positionalObjectiveCardArray = gson.fromJson(reader, PositionalObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return positionalObjectiveCardArray;
    }
}
