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
     * @return true if the angle is playable, false otherwise
     */
    public boolean isPlayable() { return playable; }

    /**
     *
     * @return the status of the angle
     */
    public AngleStatus getAngleStatus() { return angleStatus; }

    /**
     *
     * @return the resource on the angle
     */
    public Resource getResource() {
        return resource;
    }

    /**
     *
     * @param angleStatus Represent the status of the angle
     */
    private void setAngleStatus(AngleStatus angleStatus) {
        this.angleStatus = angleStatus;
    }

    /**
     *
     * @param linkedAngle The reference to the angle linked with this angle
     * @param angleStatus Represent the status of the angle
     * @throws AngleAlreadyLinkedException Exception thrown when the angle is already occupied
     */
    public void setLinkedAngle(Angle linkedAngle, AngleStatus angleStatus) throws AngleAlreadyLinkedException
    {
        if(angleStatus != AngleStatus.UNLINKED) throw new AngleAlreadyLinkedException();
        this.linkedAngle = linkedAngle;
        setAngleStatus(angleStatus);
    }

    /**
     *
     * @return the angle linked with this angle
     * @throws UnlinkedCardException Exception thrown if the angle is unlinked
     */
    public Angle getLinkedAngle() throws UnlinkedCardException {
        if(angleStatus == AngleStatus.UNLINKED)
            throw new UnlinkedCardException();
        return linkedAngle;
    }

    /**
     *
     * @return the reference to the owner card of this angle
     */
    public StarterCard getOwnerCard()
    {
        return ownerCard;
    }
}
