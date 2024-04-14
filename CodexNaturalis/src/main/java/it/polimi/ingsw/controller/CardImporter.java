package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class CardImporter {
    public ArrayList<Card> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        return factory.createGoldCardList();
    }
    public ArrayList<Card> importResourceCards() throws CardNotImportedException {
        ResourceCardFactory factory = new ResourceCardFactory();
        return factory.createResourceCardList();
    }
    public ArrayList<StarterCard> importStarterCards() throws CardNotImportedException {
        StarterCardFactory factory = new StarterCardFactory();
        return factory.createStarterCardList();
    }
    public ArrayList<ObjectiveCard> importPositionalObjectiveCards() throws CardNotImportedException {
        PositionalObjectiveCardFactory factory = new PositionalObjectiveCardFactory();
        return factory.createPositionalObjectiveCardList();
    }
    public ArrayList<ObjectiveCard> importResourceObjectiveCards() throws CardNotImportedException {
        ResourceObjectiveCardFactory factory = new ResourceObjectiveCardFactory();
        return factory.createResourceObjectiveCardList();
    }
    public ObjectiveCard importTripleObjectiveCard() throws CardNotImportedException {
        TripleObjectiveCardFactory factory = new TripleObjectiveCardFactory();
        return factory.createTripleObjectiveCard();
    }
}
