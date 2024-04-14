package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ResourceCardFactory {
    public ResourceCard[] createResourceCardArray() throws CardNotImportedException {
        ResourceCard[] resourceCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/resourceCards.json")) {
            resourceCardArray = gson.fromJson(reader, ResourceCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return resourceCardArray;
    }
}
