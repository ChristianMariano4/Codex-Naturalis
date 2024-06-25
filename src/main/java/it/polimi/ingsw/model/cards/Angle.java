package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.AngleAlreadyLinkedException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;

import java.io.Serializable;

/**
 * This class represents an angle of a card.
 * It contains the resource on the angle, the status of the angle and the reference to the linked angle.
 */
public class Angle implements Serializable {
    private final boolean playable;
    private AngleStatus angleStatus;
    private final Resource resource;
    private Angle linkedAngle;
    private final PlayableCard ownerCard;

    /**
     * Constructor
     * @param playable True if the angle is playable, false otherwise
     * @param resource Resource on the angle
     * @param ownerCard Reference to the card that have this angle
     */
    public Angle(boolean playable, Resource resource, PlayableCard ownerCard)
    {
        this.playable = playable;
        this.resource = resource;
        this.angleStatus = AngleStatus.UNLINKED;
        this.ownerCard = ownerCard;
    }

    /**
     * Get if the angle is playable
     * @return true if the angle is playable, false otherwise
     */
    public boolean isPlayable() { return playable; }

    /**
     * Get the status of the angle (unlinked, over or under)
     * @return the status of the angle
     */
    public AngleStatus getAngleStatus() { return angleStatus; }

    /**
     * Get the resource on the angle
     * @return the resource on the angle
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Set the linked angle
     * @param linkedAngle The reference to the angle linked with this angle
     * @param angleStatus Represent the status of the angle
     * @throws AngleAlreadyLinkedException Exception thrown when the angle is already occupied
     */
    public void setLinkedAngle(Angle linkedAngle, AngleStatus angleStatus) throws AngleAlreadyLinkedException
    {
        if(this.angleStatus != AngleStatus.UNLINKED) throw new AngleAlreadyLinkedException();
        this.linkedAngle = linkedAngle;
        this.angleStatus = angleStatus;
    }

    /**
     * Get the linked angle
     * @return the angle linked with this angle
     * @throws UnlinkedCardException Exception thrown if the angle is unlinked
     */
    public Angle getLinkedAngle() throws UnlinkedCardException {
        if(angleStatus == AngleStatus.UNLINKED)
            throw new UnlinkedCardException();
        return linkedAngle;
    }

    /**
     * Get the reference to the owner card of this angle
     * @return the reference to the owner card of this angle
     */
    public PlayableCard getOwnerCard()
    {
        return ownerCard;
    }
}
