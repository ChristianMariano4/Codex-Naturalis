package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.AngleAlreadyLinkedException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;


public class Angle {
    private final boolean playable;
    private AngleStatus angleStatus;
    private final Resource resource;
    private Angle linkedAngle;
    private final StarterCard ownerCard;

    /**
     *
     * @param playable True if the angle is playable, false otherwise
     * @param resource Resource on the angle
     * @param ownerCard Reference to the card that have this angle
     */
    public Angle(boolean playable, Resource resource, StarterCard ownerCard)
    {
        this.playable = playable;
        this.resource = resource;
        this.angleStatus = AngleStatus.UNLINKED;
        this.ownerCard = ownerCard;
    }

    /**
     *
     * @return
     */
    public boolean isPlayable() { return playable; }
    public AngleStatus getAngleStatus() { return angleStatus; }
    public Resource getResource() {
        return resource;
    }
    private void setAngleStatus(AngleStatus angleStatus) {
        this.angleStatus = angleStatus;
    }
    public void setLinkedAngle(Angle linkedAngle, AngleStatus angleStatus) throws AngleAlreadyLinkedException
    {
        if(angleStatus != AngleStatus.UNLINKED) throw new AngleAlreadyLinkedException();
        this.linkedAngle = linkedAngle;
        setAngleStatus(angleStatus);
    }
    public Angle getLinkedAngle() throws UnlinkedCardException {
        if(angleStatus == AngleStatus.UNLINKED)
            throw new UnlinkedCardException();
        return linkedAngle;
    }
    public StarterCard getOwnerCard()
    {
        return ownerCard;
    }
}
