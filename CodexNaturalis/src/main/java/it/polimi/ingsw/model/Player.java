package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;

import java.util.HashMap;

public class Player {
    private final String userName;
    private boolean isTurn;
    private boolean isFirst;
    private final Marker marker;
    private final HashMap<Resource, Integer>  resourceAmount;
    private int points;

    public Player(String userName, Marker marker) {
        this.userName = userName;
        this.marker = marker;
        resourceAmount = new HashMap<Resource, Integer>();

        resourceAmountInitializer(resourceAmount);
    }

    public String getUserName() {
        return userName;
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

}
