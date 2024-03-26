package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.AlreadyFourAngles;
import it.polimi.ingsw.exceptions.AlreadyPresentAngle;
import it.polimi.ingsw.exceptions.InvalidCardConstructorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class StarterCard extends Card{

    private final ArrayList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;

    public StarterCard(int cardId, Side currentSide, LinkedList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles) throws InvalidCardConstructorData {
        super(cardId, currentSide);
        try {
            this.centralResources = new ArrayList<>(centralResources); //controller can pass empty list
            this.angles = new HashMap<>(angles);
        }
        catch(Exception e)
        {
            throw new InvalidCardConstructorData();
        }

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
        StarterCard that = (StarterCard) o;
        return this.getCardId() == that.getCardId() && this.getCurrentSide() == that.getCurrentSide();
    }

    @Override
    public int hashCode() {
        return Objects.hash(centralResources, angles);
    }
}


