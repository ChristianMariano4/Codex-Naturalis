package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.PlayableCardDeck;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceCardFactory {
    public ArrayList<PlayableCard> createResourceCardList() throws CardNotImportedException {
        ResourceCard[] resourceCardArray;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("CodexNaturalis/resources/resourceCards.json")) {
            resourceCardArray = gson.fromJson(reader, ResourceCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(resourceCardArray));
    }
}
