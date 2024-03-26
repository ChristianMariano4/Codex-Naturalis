package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.ResourceCard;

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
    public Player(String username, Marker marker, Game game) throws InvalidConstructorDataException {
        try {
            this.username = username;
            this.marker = marker;
            this.game = game;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
        this.playerHand  = new PlayerHand();
        this.playerField = new PlayerField();

        resourceAmount = new HashMap<Resource, Integer>();

        resourceAmountInitializer(resourceAmount);
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

    public void updateResourceAmount(Resource resource, int amount) throws NoneResourceException {
        if(resource.equals(Resource.NONE))
            throw new NoneResourceException();
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
            if(res != Resource.NONE)
                resourceAmount.put(res, 0);
        }
    }

    public void chooseGoldCardToDraw(DrawPosition drawPosition) throws AlreadyThreeCardsInHandException, NoCardAddedException {
        try {
            ResourceCard chosenCard = game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(drawPosition);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch (DeckIsEmptyException e)
        {
            throw new NoCardAddedException();
        }

    }
    public void chooseResourceCardToDraw(DrawPosition draw) throws AlreadyThreeCardsInHandException, NoCardAddedException {
        try {
            ResourceCard chosenCard = game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(draw);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch(DeckIsEmptyException e)
        {
            throw new NoCardAddedException();
        }
    }
    public PlayerField getPlayerField()
    {
        return playerField;
    }

}
