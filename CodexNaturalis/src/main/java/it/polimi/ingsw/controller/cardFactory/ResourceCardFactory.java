package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceCardFactory extends CardFactory<ResourceCard> {

    public ArrayList<ResourceCard> createCardList() throws CardNotImportedException {
        ResourceCard[] resourceCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("/resourceCards.json"))) {
            resourceCardArray = gson.fromJson(reader, ResourceCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(resourceCardArray));
    }
}