package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.AlreadyFourAngles;
import it.polimi.ingsw.exceptions.AlreadyPresentAngle;
import it.polimi.ingsw.exceptions.InvalidCardConstructorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class StarterCard extends Card{

    private final ArrayList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;

    public StarterCard(int cardId, Side currentSide, ArrayList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles) throws InvalidCardConstructorData {
        super(cardId, currentSide);
        try {
            this.centralResources = new ArrayList<>(centralResources); //controller can pass list with NONE value
            this.angles = new HashMap<>(angles);
        }
        catch(Exception e)
        {
            throw new InvalidCardConstructorData();
        }

    }
    public ArrayList<Resource> getCentralResources()
    {
        return new ArrayList<Resource>(this.centralResources);
    }

}


