package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.AlreadyFourAngles;
import it.polimi.ingsw.exceptions.AlreadyPresentAngle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class CardWithAngles extends Card{

    private final LinkedList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;

    //TODO: add exceptions for empty sets
    public CardWithAngles(int cardId, Side currentSide, LinkedList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles) {
        super(cardId, currentSide);
        this.centralResources = new LinkedList<Resource>();
        this.angles = new HashMap<AngleOrientation, Angle>();
        try {
            this.centralResources.addAll(centralResources);
        } catch (NullPointerException e) { }
        try {
            this.angles.putAll(angles);
        } catch (NullPointerException e) { }
    }

    public LinkedList<Resource> getCentralResources()
    {
        return new LinkedList<Resource>(this.centralResources);
    }

    public void addAngle(AngleOrientation angleOrientation, Angle angle) throws AlreadyFourAngles, AlreadyPresentAngle {
        if(angles.size() >= 4) throw new AlreadyFourAngles();
        if(angles.get(angleOrientation) != null) throw new AlreadyPresentAngle();
        angles.put(angleOrientation, angle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardWithAngles that = (CardWithAngles) o;
        return this.getCardId() == that.getCardId() && this.getCurrentSide() == that.getCurrentSide();
    }

    @Override
    public int hashCode() {
        return Objects.hash(centralResources, angles);
    }
}


