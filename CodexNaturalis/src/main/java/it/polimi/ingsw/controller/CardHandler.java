package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.cardFactory.*;
import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;
import it.polimi.ingsw.model.CardVisitor;
import it.polimi.ingsw.model.CardVisitorImpl;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CardHandler {


    private final ArrayList<CardPair<ResourceCard>> resourceCards;
    private final ArrayList<CardPair<GoldCard>> goldCards;
    private final ArrayList<CardPair<StarterCard>> starterCards;
    private final ArrayList<CardPair<ObjectiveCard>> objectiveCards;
    private final ArrayList<CardPair<PlayableCard>> playableCards;
    /**
     * Constructor
     */
    public CardHandler()
    {
        this.resourceCards = new ArrayList<>();
        this.goldCards = new ArrayList<>();
        this.starterCards = new ArrayList<>();
        this.objectiveCards = new ArrayList<>();
        this.playableCards = new ArrayList<>();
    }

    /**
     * Create a list of GoldCards
     * @return the list of gold cards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<GoldCard> importGoldCards() throws CardNotImportedException {
        GoldCardFactory factory = new GoldCardFactory();
        ArrayList<GoldCard> cardList = factory.createCardList();
        linkGoldCards(cardList);
        return cardList;
    }

    /**
     * Create a list of ResourceCards
     * @return the list of resource cards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<ResourceCard> importResourceCards() throws CardNotImportedException {
        ResourceCardFactory factory = new ResourceCardFactory();
        ArrayList<ResourceCard> cardList = factory.createCardList();
        linkResourceCards(cardList);
        return cardList;
    }

    /**
     * Create a list of StarterCards
     * @return the list of starter cards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<StarterCard> importStarterCards() throws CardNotImportedException {
        StarterCardFactory factory = new StarterCardFactory();
        ArrayList<StarterCard> cardList = factory.createCardList();
        linkStarterCards(cardList);
        return cardList;
    }

    /**
     * Create a list of PositionalObjectiveCards
     * @return the list of PositionalObjectiveCards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<ObjectiveCard> importPositionalObjectiveCards() throws CardNotImportedException {
        PositionalObjectiveCardFactory factory = new PositionalObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }

    /**
     * Create a list of ResourceObjectiveCards
     * @return the list of ResourceObjectiveCards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<ObjectiveCard> importResourceObjectiveCards() throws CardNotImportedException {
        ResourceObjectiveCardFactory factory = new ResourceObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }

    /**
     * Create a list of TripleObjectiveCards
     * @return the list of TripleObjectiveCards
     * @throws CardNotImportedException if a card is not imported correctly
     */
    public ArrayList<ObjectiveCard> importTripleObjectiveCard() throws CardNotImportedException {
        TripleObjectiveCardFactory factory = new TripleObjectiveCardFactory();
        ArrayList<ObjectiveCard> cardList = factory.createCardList();
        linkObjectiveCards(cardList);
        return cardList;
    }

    /**
     * Link all GoldCards with their other side
     * @param cardList is the list of GoldCards
     */
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

    /**
     * Link all ResourceCards with their other side
     * @param cardList is the list of ResourceCards
     */
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

    /**
     * Link all StarterCards with their other side
     * @param cardList is the list of StarterCards
     */
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

    /**
     * Link all ObjectiveCards with their other side
     * @param cardList is the list of ObjectiveCards
     */
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

    /**
     * Link all PlayableCards with their other side
     * @param cardList is the list of PlayableCards
     */
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

    /**
     * Remove from the Resource cardList all the Side.BACK cards
     * @param cardList is the list of ResourceCards
     * @return the list without all the Side.BACK cards
     */
    public ArrayList<ResourceCard> filterResourceCards(ArrayList<ResourceCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }

    /**
     * Remove from the Gold cardList all the Side.BACK cards
     * @param cardList is the list of GoldCards
     * @return the list without all the Side.BACK cards
     */
    public ArrayList<GoldCard> filterGoldCards(ArrayList<GoldCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }
    /**
     * Remove from the Starter cardList all the Side.BACK cards
     * @param cardList is the list of StarterCards
     * @return the list without all the Side.BACK cards
     */
    public ArrayList<StarterCard> filterStarterCards(ArrayList<StarterCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());

    }
    /**
     * Remove from the Objective cardList all the Side.BACK cards
     * @param cardList is the list of ObjectiveCards
     * @return the list without all the Side.BACK cards
     */
    public ArrayList<ObjectiveCard> filterObjectiveCards(ArrayList<ObjectiveCard> cardList) {
        return new ArrayList<>(cardList.stream().filter(c -> c.getCurrentSide().equals(Side.FRONT)).toList());
    }

    /**
     * Get the other side of the selected PlayableCard
     * @param card is the selected PlayableCard
     * @return the other side of the selected PlayableCard
     */
    public PlayableCard getOtherSideCard(PlayableCard card) {

        return playableCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c-> {
            try {
                return c.getOtherSideCard(card.getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

    /**
     * Get the other side of the selected GoldCard
     * @param card is the selected GoldCard
     * @return the other side of the selected GoldCard
     */
    public GoldCard getOtherSideCard(GoldCard card)
    {
        return goldCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c-> {
            try {
                return c.getOtherSideCard(card.getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

    /**
     * Get the other side of the selected ResourceCard
     * @param card is the selected ResourceCard
     * @return the other side of the selected ResourceCard
     */
    public ResourceCard getOtherSideCard(ResourceCard card)
    {
        return resourceCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c-> {
            try {
                return c.getOtherSideCard(card.getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

    /**
     * Get the other side of the selected StarterCard
     * @param card is the selected StarterCard
     * @return the other side of the selected StarterCard
     */
    public StarterCard getOtherSideCard(StarterCard card)
    {
        return starterCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c-> {
            try {
                return c.getOtherSideCard(card.getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

    /**
     * Get the other side of the selected ObjectiveCard
     * @param card is the selected ObjectiveCard
     * @return the other side of the selected ObjectiveCard
     */
    public ObjectiveCard getOtherSideCard(ObjectiveCard card)
    {
        return objectiveCards.stream().filter(c -> c.getCardsId() == card.getCardId()).map(c-> {
            try {
                return c.getOtherSideCard(card.getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

    /**
     * Check if the selected card Requirements are satisfied by the player
     * @param card is the selected card
     * @param player is the player how want to play the card
     * @return true if the player can use the card, false otherwise
     * @throws CardTypeMismatchException if the cardType is not a RESOURCE or GOLD card
     */
    public boolean checkRequirements(PlayableCard card, Player player) throws CardTypeMismatchException {
        CardVisitor visitor = new CardVisitorImpl();
        CardInfo cardInfo = card.accept(visitor);
        if(cardInfo.getCardType().equals(CardType.RESOURCE))
        {
            return true;
        }
        if(cardInfo.getCardType().equals(CardType.GOLD))
        {
            if(card.getCurrentSide().equals(Side.BACK))
                return true;
            HashMap<Resource, Integer> requirementsResourceAmount = new HashMap<>();
            for(Resource resource : cardInfo.getRequirements())
            {
                //merge adds 1 if key exists otherwise sets value to 1
                //TODO: check correct
                requirementsResourceAmount.merge(resource, 1, Integer::sum);
            }
            for(Resource resource : requirementsResourceAmount.keySet())
            {
               if(player.getResourceAmount(resource) < requirementsResourceAmount.get(resource))
                   return false;
            }
            return true;
        }
        throw new CardTypeMismatchException();
    }
    public PlayableCard getPlayableCardById(int cardId)
    {
        return playableCards.stream().filter((c -> c.getCardsId() == cardId)).map(c -> {
            try {
                return c.getOtherSideCard(Side.BACK);
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElseThrow();
    }
    public CardInfo getCardInfo(Card card)
    {
        CardVisitor visitor = new CardVisitorImpl();
        return card.accept(visitor);
    }

}
