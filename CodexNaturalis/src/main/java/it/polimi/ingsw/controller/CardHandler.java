package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.cardFactory.*;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class CardHandler {


    private final ArrayList<CardPair<ResourceCard>> resourceCards;
    private final ArrayList<CardPair<GoldCard>> goldCards;
    private final ArrayList<CardPair<StarterCard>> starterCards;
    private final ArrayList<CardPair<ObjectiveCard>> objectiveCards;
    private final ArrayList<CardPair<PlayableCard>> playableCards;

    public CardHandler()
    {
        this.resourceCards = new ArrayList<>();
        this.goldCards = new ArrayList<>();
        this.starterCards = new ArrayList<>();
        this.objectiveCards = new ArrayList<>();
        this.playableCards = new ArrayList<>();
    }

    public ArrayList<GoldCard> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        ArrayList<GoldCard> cardList = factory.createCardList();
        linkGoldCards(cardList);
        return cardList;
    }
    public ArrayList<ResourceCard> importResourceCards() throws CardNotImportedException {
        ResourceCardFactory factory = new ResourceCardFactory();
        ArrayList<ResourceCard> cardList = factory.createCardList();
        linkResourceCards(cardList);
        return cardList;
    }
    public ArrayList<StarterCard> importStarterCards() throws CardNotImportedException {
        StarterCardFactory factory = new StarterCardFactory();
        ArrayList<StarterCard> cardList = factory.createCardList();
        linkStarterCards(cardList);
        return cardList;
    }
    public ArrayList<ObjectiveCard> importPositionalObjectiveCards() throws CardNotImportedException {
        PositionalObjectiveCardFactory factory = new PositionalObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }
    public ArrayList<ObjectiveCard> importResourceObjectiveCards() throws CardNotImportedException {
        ResourceObjectiveCardFactory factory = new ResourceObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }
    public ArrayList<ObjectiveCard> importTripleObjectiveCard() throws CardNotImportedException {
        TripleObjectiveCardFactory factory = new TripleObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }
    private void linkGoldCards(ArrayList<GoldCard> cardList)
    {
        cardList.forEach(c1 ->
                {
                    if(c1.getCurrentSide() == Side.FRONT) {
                        this.goldCards.add(new CardPair<>(c1, cardList.stream()
                                .filter(c2 -> c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                                .findFirst()
                                .orElse(null)));
                    }
                }
             );
        linkPlayableCards(new ArrayList<>(cardList));
    }
    private void linkResourceCards(ArrayList<ResourceCard> cardList)
    {
        cardList.forEach(c1 ->
                {
                    if(c1.getCurrentSide() == Side.FRONT) {
                        this.resourceCards.add(new CardPair<>(c1, cardList.stream()
                                .filter(c2 -> c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                                .findFirst()
                                .orElse(null)));
                    }
                }
        );
        linkPlayableCards(new ArrayList<>(cardList));

    }
    private void linkStarterCards(ArrayList<StarterCard> cardList)
    {
        cardList.forEach(c1 ->
                {
                    if(c1.getCurrentSide() == Side.FRONT) {
                        this.starterCards.add(new CardPair<>(c1, cardList.stream()
                                .filter(c2 -> c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                                .findFirst()
                                .orElse(null)));
                    }
                }
        );
        linkPlayableCards(new ArrayList<>(cardList));

    }
    private void linkObjectiveCards(ArrayList<ObjectiveCard> cardList)
    {
        cardList.forEach(c1 ->
                {
                    if(c1.getCurrentSide() == Side.FRONT) {
                        this.objectiveCards.add(new CardPair<>(c1, cardList.stream()
                                .filter(c2 -> c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                                .findFirst()
                                .orElse(null)));
                    }
                }
        );

    }

    public void linkPlayableCards(ArrayList<PlayableCard> cardList)
    {
        cardList.forEach(c1 ->
                {
                    if(c1.getCurrentSide() == Side.FRONT) {
                        this.playableCards.add(new CardPair<>(c1, cardList.stream()
                                .filter(c2 -> c1.getCardId() == c2.getCardId() && c1.getCurrentSide() != c2.getCurrentSide())
                                .findFirst()
                                .orElse(null)));
                    }
                }
        );
    }
    public ArrayList<ResourceCard> filterResourceCards(ArrayList<ResourceCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.BACK)).toList());

    }
    public ArrayList<GoldCard> filterGoldCards(ArrayList<GoldCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.BACK)).toList());

    }
    public ArrayList<StarterCard> filterStarterCards(ArrayList<StarterCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.BACK)).toList());

    }

    public ArrayList<ObjectiveCard> filterObjectiveCards(ArrayList<ObjectiveCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.BACK)).toList());
    }
    public PlayableCard getOtherSideCard(PlayableCard card) throws CardNotFoundException {

        return playableCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c->c.getOtherSideCard(card.getCurrentSide())).findFirst().orElse(null);
    }

    public GoldCard getOtherSideCard(GoldCard card)
    {
        return goldCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c->c.getOtherSideCard(card.getCurrentSide())).findFirst().orElse(null);
    }
    public ResourceCard getOtherSideCard(ResourceCard card)
    {
        return resourceCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c->c.getOtherSideCard(card.getCurrentSide())).findFirst().orElse(null);
    }
    public StarterCard getOtherSideCard(StarterCard card)
    {
        return starterCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c->c.getOtherSideCard(card.getCurrentSide())).findFirst().orElse(null);
    }
    public ObjectiveCard getOtherSideCard(ObjectiveCard card)
    {
        return objectiveCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c->c.getOtherSideCard(card.getCurrentSide())).findFirst().orElse(null);
    }

}
