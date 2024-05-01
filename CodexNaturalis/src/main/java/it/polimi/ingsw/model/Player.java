package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents the player
 */
public class Player implements Serializable {
    private final String username;
    private boolean isTurn;
    private boolean isFirst;
    private Marker marker;
    private final HashMap<Resource, Integer>  resourceAmount;
    private int points;
    private final PlayerHand playerHand;
    private final PlayerField playerField;
    private final Game game;
    private ObjectiveCard secretObjective;
    private StarterCard starterCard;

    /**
     * Constructor
     * @param username The ID of the player
     * @param game The reference of the current game
     * @throws InvalidConstructorDataException Exception thrown if the constructor data are invalid
     */
    //controller crates player -> player creates empty playerhand -> controller adds cards to playerhand from deck
    public Player(String username, Game game) throws InvalidConstructorDataException {
        try {
            this.username = username;
            this.game = game;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
        this.playerHand  = new PlayerHand();
        this.playerField = new PlayerField();
        this.points = 0;

        resourceAmount = new HashMap<Resource, Integer>();

        resourceAmountInitializer(resourceAmount);
    }

    /**
     * Getter
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter
     * @return a boolean that indicate if is the turn of the player
     */
    public boolean getIsTurn() {
        return isTurn;
    }

    /**
     * Setter
     * @param isTurn True if is the turn of the player, false otherwise
     */
    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    /**
     * Getter
     * @return true if the player is the first of the game
     */
    public boolean getIsFirst() {
        return isFirst;
    }

    /**
     * Setter
     * @param isFirst Is true if the player is the first of the game, false otherwise
     */
    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    /**
     * Getter
     * @return the Marker of the player
     */
    public Marker getMarker() {
        return marker;
    }
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     * Getter
     * @param resource Resource of which we want to know the quantity possessed by the player
     * @return the amount of the specified resource of the player
     */
    public int getResourceAmount(Resource resource) {
        return this.resourceAmount.get(resource);
    }

    /**
     * Update the amount of a specific resource
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
     * Getter
     * @return the points of the player
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Add points to the player
     * @param points The value to add to the points of the player
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Initialize all the resources of the player to 0
     * @param resourceAmount HashMap used to map a resource into the amount owned by the player, if NONE is set to -1 a not valid value
     */
    private void resourceAmountInitializer(HashMap<Resource, Integer> resourceAmount) {
        for(Resource res: Resource.values()) {
            if(res != Resource.NONE) {
                resourceAmount.put(res, 0);
            } else {
                resourceAmount.put(res, -1);
            }
        }
    }

    /**
     * Method used to choose a gold card to draw
     * @param drawPosition Indicate the position from which to draw a card
     * @throws AlreadyThreeCardsInHandException Exception thrown when the player have already tre cards in its hand
     * @throws NoCardAddedException Exception thrown when no card is added to the player hand
     */
    public void chooseGoldCardToDraw(DrawPosition drawPosition) throws AlreadyThreeCardsInHandException, NoCardAddedException {
        try {
            GoldCard chosenCard = game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(drawPosition);
            playerHand.addCardToPlayerHand(chosenCard);
        }
        catch (DeckIsEmptyException e)
        {
            throw new NoCardAddedException();
        }

    }

    /**
     * Method used to choose a resource card to draw
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
     * Getter
     * @return the reference of the playerFiled linked to the player
     */
    public PlayerField getPlayerField() {
        return playerField;
    }
    public PlayerHand getPlayerHand() { return playerHand; }
    public void setSecretObjective(ObjectiveCard secretObjective)
    {
        this.secretObjective = secretObjective;
    }
    public ObjectiveCard getSecretObjective()
    {
        return this.secretObjective;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }


}
