package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ResourceObjectiveCardFactory {
    public ResourceObjectiveCard[] resourceObjectiveCardArray() throws CardNotImportedException {
        ResourceObjectiveCard[] resourceObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/resourceObjectiveCards.json")) {
            resourceObjectiveCardArray = gson.fromJson(reader, ResourceObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return resourceObjectiveCardArray;
    }
}
