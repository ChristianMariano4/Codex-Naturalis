package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.cardFactory.*;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class CardHandler {
    public ArrayList<GoldCard> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        return linkGoldCards(factory.createCardList());
    }
    public ArrayList<ResourceCard> importResourceCards() throws CardNotImportedException {
        ResourceCardFactory factory = new ResourceCardFactory();
        return linkResourceCards(factory.createCardList());
    }
    public ArrayList<StarterCard> importStarterCards() throws CardNotImportedException {
        StarterCardFactory factory = new StarterCardFactory();
        return linkStarterCards(factory.createCardList());
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
    public ArrayList<GoldCard> linkGoldCards(ArrayList<GoldCard> cardList)
    {

        cardList.forEach(c1 ->
                c1.setOtherSideCard(cardList.stream()
                        .filter(c2 ->c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                        .findFirst()
                        .orElse(null)));
        return cardList;

    }
    public ArrayList<ResourceCard> linkResourceCards(ArrayList<ResourceCard> cardList)
    {

        cardList.forEach(c1 ->
                c1.setOtherSideCard(cardList.stream()
                        .filter(c2 ->c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                        .findFirst()
                        .orElse(null)));
        return cardList;

    }
    public ArrayList<StarterCard> linkStarterCards(ArrayList<StarterCard> cardList)
    {

        cardList.forEach(c1 ->
                c1.setOtherSideCard(cardList.stream()
                        .filter(c2 ->c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                        .findFirst()
                        .orElse(null)));
        return cardList;

    }
    public ArrayList<ResourceCard> filterResourceCards(ArrayList<ResourceCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }
    public ArrayList<GoldCard> filterGoldCards(ArrayList<GoldCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }
    public ArrayList<StarterCard> filterStarterCards(ArrayList<StarterCard> cardList) {
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
