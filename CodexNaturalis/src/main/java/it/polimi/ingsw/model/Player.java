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

    /**
     * Controller
     * @param username The ID of the player
     * @param marker The unique marker of the player
     * @param game The reference of the current game
     * @throws InvalidConstructorDataException Exception thrown if the constructor data are invalid
     */
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

    /**
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return a boolean that indicate if is the turn of the player
     */
    public boolean getIsTurn() {
        return isTurn;
    }

    /**
     *
     * @param isTurn True if is the turn of the player, false otherwise
     */
    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    /**
     *
     * @return true if the player is the first of the game
     */
    public boolean getIsFirst() {
        return isFirst;
    }

    /**
     *
     * @param isFirst Is true if the player is the first of the game, false otherwise
     */
    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    /**
     *
     * @return the Marker of the player
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     *
     * @param resource Resource of which we want to know the quantity possessed by the player
     * @return the amount of the specified resource of the player
     */
    public int getResourceAmount(Resource resource) {
        return this.resourceAmount.get(resource);
    }

    /**
     *
     * @param resource Resource of witch we want to update the value
     * @param amount The value to add to the existing value of a resource
     * @throws NoneResourceException Exception thrown if resource in NONE
     */
    public void updateResourceAmount(Resource resource, int amount) throws NoneResourceException {
        if(resource.equals(Resource.NONE))
            throw new NoneResourceException();
        int temp = resourceAmount.get(resource);
        temp += amount;
        resourceAmount.put(resource, temp);
    }

    /**
     *
     * @return the points of the player
     */
    public int getPoints() {
        return this.points;
    }

    /**
     *
     * @param points The value to add to the points of the player
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     *
     * @param resourceAmount HashMap used to link a resource to the corresponding amount
     */
    private void resourceAmountInitializer(HashMap<Resource, Integer> resourceAmount) {
        for(Resource res: Resource.values()) {
            if(res != Resource.NONE)
                resourceAmount.put(res, 0);
        }
    }

    /**
     *
     * @param drawPosition Indicate the position from which to draw a card
     * @throws AlreadyThreeCardsInHandException Exception thrown when the player have already tre cards in its hand
     * @throws NoCardAddedException Exception thrown when no card is added to the player hand
     */
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

    /**
     *
     * @param drawPosition Indicate the position from which to draw a card
     * @throws AlreadyThreeCardsInHandException Exception thrown when the player have already tre cards in its hand
     * @throws NoCardAddedException Exception thrown when no card is added to the player hand
     */
    public void chooseResourceCardToDraw(DrawPosition drawPosition) throws AlreadyThreeCardsInHandException, NoCardAddedException {
        try {
            ResourceCard chosenCard = game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(drawPosition);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch(DeckIsEmptyException e)
        {
            throw new NoCardAddedException();
        }
    }

    /**
     *
     * @return the reference of the playerFiled linked to the player
     */
    public PlayerField getPlayerField()
    {
        return playerField;
    }

}
