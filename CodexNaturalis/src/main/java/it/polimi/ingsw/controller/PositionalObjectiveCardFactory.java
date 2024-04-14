package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class PositionalObjectiveCardFactory {
    public ArrayList<ObjectiveCard> createPositionalObjectiveCardList() throws CardNotImportedException {
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
