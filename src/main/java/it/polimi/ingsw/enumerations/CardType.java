package it.polimi.ingsw.enumerations;

public enum CardType {
    RESOURCE,
    GOLD,
    STARTER,
    TRIPLEOBJECTIVE,
    RESOURCEOBJECTIVE,
    POSITIONALOBJECTIVE;

    public String toString() {
        return switch (this) {
            case RESOURCE -> "Resource";
            case GOLD -> "Gold";
            case STARTER -> "Starter";
            case TRIPLEOBJECTIVE, RESOURCEOBJECTIVE, POSITIONALOBJECTIVE -> "Objective";
        };
    }
}


