package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHand;
import it.polimi.ingsw.exceptions.DeckIsEmpty;
import it.polimi.ingsw.exceptions.InvalidCardPosition;
import it.polimi.ingsw.exceptions.NoCardAdded;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.HashMap;

public class Player {
    private final String username;
    private boolean isTurn;
    private boolean isFirst;
    private final Marker marker;
    private final HashMap<Resource, Integer>  resourceAmount;
    private int points;
    private final PlayerHand playerHand;
    private final PlayerField playerField;
    private final Game game;


    //controller crates player -> player creates empty playerhand -> controller adds cards to playerhand from deck
    public Player(String username, Marker marker, Game game) {
        this.username = username;
        this.marker = marker;
        this.game = game;
        this.playerHand  = new PlayerHand();
        this.playerField = new PlayerField();

        resourceAmount = new HashMap<Resource, Integer>();

        resourceAmountInitializer(resourceAmount);
    }
    public void playCard(PlayableCard cardOnField, AngleOrientation angleOrientation, PlayableCard cardToPlay) throws InvalidCardPosition {
        try {
            playerField.addCardToCell(cardOnField, angleOrientation, cardToPlay);
            playerHand.removeCardFromHand(cardToPlay);
        }
        catch(InvalidCardPosition e)
        {
            throw e;
        }

    }

    public String getUsername() {
        return username;
    }

    public boolean getIsTurn() {
        return isTurn;
    }

    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Marker getMarker() {
        return marker;
    }

    public int getResourceAmount(Resource resource) {
        return this.resourceAmount.get(resource);
    }

    public void updateResourceAmount(Resource resource, int amount) {
        int temp = resourceAmount.get(resource);
        temp += amount;
        resourceAmount.put(resource, temp);
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    private void resourceAmountInitializer(HashMap<Resource, Integer> resourceAmount) {
        for(Resource res: Resource.values()) {
            resourceAmount.put(res, 0);
        }
    }

    public void chooseGoldCardToDraw(ChooseDrawPosition draw) throws AlreadyThreeCardsInHand, NoCardAdded {
        try {
            PlayableCard chosenCard = game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(draw);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch (DeckIsEmpty e)
        {
            throw new NoCardAdded();
        }

    }
    public void chooseResourceCardToDraw(ChooseDrawPosition draw) throws AlreadyThreeCardsInHand, NoCardAdded {
        try {
            PlayableCard chosenCard = game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(draw);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch(DeckIsEmpty e)
        {
            throw new NoCardAdded();
        }
    }
    public PlayerField getPlayerField()
    {
        return playerField;
    }

}
