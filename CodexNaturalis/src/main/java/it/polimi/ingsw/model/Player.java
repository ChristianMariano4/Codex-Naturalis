package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.GameObject;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String userName;
    private boolean isTurn;
    private boolean isFirst;
    private Marker marker;
    private HashMap<Resource, Integer>  resourceAmount;
    private HashMap<GameObject, Integer> objectAmount;
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
        int temp = resourceAmount.get(resource);
        temp += amount;
        resourceAmount.put(resource, temp);
    }
    public int getObjectAmount(GameObject gameObject) {
        return this.objectAmount.get(gameObject);
    }
    public void updateObjectAmount(GameObject gameObject, int amount) {
        int temp = objectAmount.get(gameObject);
        temp += amount;
        objectAmount.put(gameObject, temp);
    }
    public int getPoints() {
        return this.points;
    }
    public void addPoints(int points) {
        this.points += points;
    }


}
