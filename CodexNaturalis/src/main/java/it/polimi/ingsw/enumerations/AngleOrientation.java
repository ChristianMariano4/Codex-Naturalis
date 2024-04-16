package it.polimi.ingsw.enumerations;

public enum AngleOrientation {
    TOPRIGHT,
    TOPLEFT,
    BOTTOMRIGHT,
    BOTTOMLEFT,
    NONE;

    public int mapEnumToX()
    {
        return switch (this) {
            case TOPRIGHT, BOTTOMRIGHT -> 1;
            case TOPLEFT, BOTTOMLEFT -> -1;
            case NONE -> 0;
        };

    }
    public int mapEnumToY()
    {
        return switch (this) {
            case BOTTOMLEFT, BOTTOMRIGHT -> 1;
            case TOPLEFT, TOPRIGHT -> -1;
            case NONE -> 0;
        };
    }

}
