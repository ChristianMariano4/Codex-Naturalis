package it.polimi.ingsw.enumerations;

public enum AngleOrientation {
    TOPRIGHT,
    TOPLEFT,
    BOTTOMRIGHT,
    BOTTOMLEFT;

    public int mapEnumToX()
    {
        return switch (this) {
            case TOPRIGHT, BOTTOMRIGHT -> 1;
            case TOPLEFT, BOTTOMLEFT -> -1;
        };

    }
    public int mapEnumToY()
    {
        return switch (this) {
            case BOTTOMLEFT, BOTTOMRIGHT -> 1;
            case TOPLEFT, TOPRIGHT -> -1;
        };
    }

}
