package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class PositionalObjectiveCardFactory extends CardFactory<ObjectiveCard> {
    private final String filePath;

    public PositionalObjectiveCardFactory(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<ObjectiveCard> createCardList() throws CardNotImportedException {
        PositionalObjectiveCard[] positionalObjectiveCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader(filePath)) {
            positionalObjectiveCardArray = gson.fromJson(reader, PositionalObjectiveCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(positionalObjectiveCardArray));
    }
}