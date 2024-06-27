package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the type of a card.
 */
public enum CardType {
    RESOURCE,
    GOLD,
    STARTER,
    TRIPLEOBJECTIVE,
    RESOURCEOBJECTIVE,
    POSITIONALOBJECTIVE;

    /**
     * This method returns the string representation of the enumeration.
     *
     * @return the string representation of the enumeration
     */
    public String toString() {
        return switch (this) {
            case RESOURCE -> "Resource";
            case GOLD -> "Gold";
            case STARTER -> "Starter";
            case TRIPLEOBJECTIVE, RESOURCEOBJECTIVE, POSITIONALOBJECTIVE -> "Objective";
        };
    }
}


