package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.PlayableCardDeck;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class CardHandler {
    public ArrayList<PlayableCard> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        /*ArrayList<Card> lits = factory.createGoldCardList();
        for(int i = 0; i< lits.size(); i++)
        {
            if(i%2 == 0) {
                lits.get(i).setOtherSideCard(lits.get(i + 1));
            }
            else{
                lits.get(i).setOtherSideCard(lits.get(i - 1));
            }
        }*/
        return factory.createGoldCardList();
    }
    public ArrayList<PlayableCard> importResourceCards() throws CardNotImportedException {
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
