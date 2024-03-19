package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.GameObject;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private final String userName;
    private boolean isTurn;
    private boolean isFirst;
    private final Marker marker;
    private HashMap<Resource, Integer>  resourceAmount;
    private HashMap<GameObject, Integer> objectAmount;
    private int points;

    public Player(String userName, Marker marker) {
        this.userName = userName;
        this.marker = marker;
        resourceAmount = new HashMap<Resource, Integer>();
        objectAmount = new HashMap<GameObject, Integer>();

        resourceAmountInitializer(resourceAmount);
        objectAmountInitializer(objectAmount);
    }

    public String getUserName() {
        return userName;
    }
/*    public void setUserName(String userName) {
        this.userName = userName;
    }*/
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
/*    public void setMarker(Marker marker) {
        this.marker = marker;
    }*/
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

    private void resourceAmountInitializer(HashMap<Resource, Integer> resourceAmount) {
        for(Resource res: Resource.values()) {
            resourceAmount.put(res, 0);
        }
    }
    private void objectAmountInitializer(HashMap<GameObject, Integer> objectAmount) {
        for(GameObject gObj: GameObject.values()) {
            objectAmount.put(gObj, 0);
        }
    }


}
