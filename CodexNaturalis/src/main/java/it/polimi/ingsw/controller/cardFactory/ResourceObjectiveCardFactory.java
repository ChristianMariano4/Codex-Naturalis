package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.cardFactory.CardFactory;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceObjectiveCardFactory extends CardFactory<ObjectiveCard> {
    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        ResourceObjectiveCard[] resourceObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/resourceObjectiveCards.json")) {
            resourceObjectiveCardArray = gson.fromJson(reader, ResourceObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(resourceObjectiveCardArray));
    }
}
