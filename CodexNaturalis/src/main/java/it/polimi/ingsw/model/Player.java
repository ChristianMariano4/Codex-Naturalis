package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String userName;
    private boolean isTurn;
    private boolean isFirst;
    private Marker marker;
    private HashMap<Resource, int>  resourceAmount;
    private HashMap<GameObj, int> objectAmount;
    private int points;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
    public int getResourceAmount(Resource resource) {
        return this.resourceAmount.get(resource);
    }
    public void updateResourceAmount(Resource resource, int amount) {
        int temp = resourceAmount.get(resurce);
        temp += amount;
        resourceAmount.put(resource, temp);
    }


}
