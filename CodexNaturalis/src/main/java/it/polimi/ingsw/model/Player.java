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
    private Game game;
    private ObjectiveCard secretObjective;
    private StarterCard starterCard;
    private boolean isDisconnected;
    private boolean isReconnecting = false;

    /**
     * Constructor
     * @param username The ID of the player
     * @throws InvalidConstructorDataException Exception thrown if the constructor data are invalid
     */
    public Player(String username) throws InvalidConstructorDataException {
        this.username = username;
        this.isDisconnected = false;
        this.playerHand  = new PlayerHand();
        this.playerField = new PlayerField();
        this.points = 0;
        this.marker = null;
        resourceAmount = new HashMap<>();

        resourceAmountInitializer(resourceAmount);
    }

    /**
     * Set the game to which the player belongs
     * @param game The reference of the game to which the player belongs
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the username of the player
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * See if it is the turn of the player
     * @return a boolean that indicate if is the turn of the player
     */
    public boolean getIsTurn() {
        return isTurn;
    }

    /**
     * Set the turn status (true or false) of the player
     * @param isTurn true if is the turn of the player, false otherwise
     */
    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    /**
     * See if the player has the first turn
     * @return true if the player is the first of the game
     */
    public boolean getIsFirst() {
        return isFirst;
    }

    /**
     * Set if the player has the first turn
     * @param isFirst is true if the player is the first of the game, false otherwise
     */
    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
        if(this.isFirst)
            this.isTurn = true;
    }

    /**
     * Get the marker chosen by the player
     * @return the Marker of the player
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * Set the marker chosen by the player
     * @param marker The marker selected by the player during the pre-game
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     * Get the amount of a specific resource
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
     * Get the points of the player
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
     * Get the player field of the player
     * @return the reference of the playerFiled linked to the player
     */
    public PlayerField getPlayerField() {
        return playerField;
    }

    /**
     * Get the player hand
     * @return the playerHand
     */
    public PlayerHand getPlayerHand() { return playerHand; }

    /**
     * Set the secret objective card of the player
     * @param secretObjective secret objective card to set
     */
    public void setSecretObjective(ObjectiveCard secretObjective)
    {
        this.secretObjective = secretObjective;
    }

    /**
     * Get the secret objective card of the player
     * @return the secret objective card
     */
    public ObjectiveCard getSecretObjective()
    {
        return this.secretObjective;
    }

    /**
     * Get the starter card of the player
     * @return the starter card
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Set the starter card of the player
     * @param starterCard to set as player starter card
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Set isDisconnected at true
     */
    public void setDisconnected(){
        this.isDisconnected = true;
    }

    /**
     * set isDisconnected at false
     */
    public void setConnected(){
        this.isDisconnected = false;
    }

    /**
     * See if the player is disconnected
     * @return isDisconnected value
     */
    public boolean getIsDisconnected(){
        return this.isDisconnected;
    }

    /**
     * set isReconnecting to true
     */
    public void setReconnecting(){
        this.isReconnecting = true;
    }

    /**
     * See if the player is reconnecting
     * @return isReconnecting value
     */
    public boolean getIsReconnecting(){
        return this.isReconnecting;
    }

}
