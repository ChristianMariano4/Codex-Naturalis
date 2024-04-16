package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.cardFactory.*;
import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CardHandler {
    public ArrayList<PlayableCard> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        return linkPlayableCards(factory.createCardList());
    }
    public ArrayList<PlayableCard> importResourceCards() throws CardNotImportedException {
        ResourceCardFactory factory = new ResourceCardFactory();
        return linkPlayableCards(factory.createCardList());
    }
    public ArrayList<PlayableCard> importStarterCards() throws CardNotImportedException {
        StarterCardFactory factory = new StarterCardFactory();
        return linkPlayableCards(factory.createCardList());
    }
    public ArrayList<ObjectiveCard> importPositionalObjectiveCards() throws CardNotImportedException {
        PositionalObjectiveCardFactory factory = new PositionalObjectiveCardFactory();
        return linkObjectiveCards(factory.createCardList());
    }
    public ArrayList<ObjectiveCard> importResourceObjectiveCards() throws CardNotImportedException {
        ResourceObjectiveCardFactory factory = new ResourceObjectiveCardFactory();
        return linkObjectiveCards(factory.createCardList());
    }
    public ArrayList<ObjectiveCard> importTripleObjectiveCard() throws CardNotImportedException {
        TripleObjectiveCardFactory factory = new TripleObjectiveCardFactory();
        return linkObjectiveCards(factory.createCardList());
    }
    public ArrayList<PlayableCard> linkPlayableCards(ArrayList<PlayableCard> cardList)
    {

        cardList.forEach(c1 ->
                c1.setOtherSideCard(cardList.stream()
                        .filter(c2 ->c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                        .findFirst()
                        .orElse(null)));
        return cardList;

    }
    public ArrayList<PlayableCard> filterPlayableCards(ArrayList<PlayableCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }
    public ArrayList<ObjectiveCard> linkObjectiveCards(ArrayList<ObjectiveCard> cardList)
    {
        cardList.forEach(c1 ->
                c1.setOtherSideCard(cardList.stream()
                        .filter(c2 ->c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                        .findFirst()
                        .orElse(null)));
        return cardList;

    }
    public ArrayList<ObjectiveCard> filterObjectiveCards(ArrayList<ObjectiveCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());
    }

}
