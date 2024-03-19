package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.unlinkedCardException;


public class Angle {
    private final boolean playable;
    private AngleStatus angleStatus;
    private final Resource resource;
    private Angle linkedAngle;
    private final CardWithAngles ownerCard;

    public Angle(boolean playable, Resource resource, CardWithAngles ownerCard)
    {
        this.playable = playable;
        this.resource = resource;
        this.angleStatus = AngleStatus.UNLINKED;
        this.ownerCard = ownerCard;
    }
    public boolean isPlayable()
    {
        return playable;
    }
    public AngleStatus getAngleStatus()
    {
        return angleStatus;
    }
    public Resource getResource() {
        return resource;
    }

    public void setAngleStatus(AngleStatus angleStatus) {
        this.angleStatus = angleStatus;
    }
    public void setLinkedAngle(Angle linkedAngle)
    {
        this.linkedAngle = linkedAngle;
    }
    public Angle getLinkedAngle() throws unlinkedCardException {
        if(angleStatus == AngleStatus.UNLINKED)
            throw new unlinkedCardException();
        return linkedAngle;
    }
    public CardWithAngles getOwnerCard()
    {
        return ownerCard;
    }
}
