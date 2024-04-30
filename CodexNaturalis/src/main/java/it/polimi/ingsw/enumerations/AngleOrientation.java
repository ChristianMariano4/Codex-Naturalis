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
    public int mapEnumToXLShaped()
    {
        int xValue;
        return switch (this) {
            case TOPRIGHT, BOTTOMLEFT -> {
                xValue = 1;
                yield xValue;
            }
            case TOPLEFT, BOTTOMRIGHT -> {
                xValue = -1;
                yield xValue;
            }
            case NONE -> 0;
        };
    }
    public int mapEnumToYLShaped()
    {
        int yValue;
        return switch(this)
        {
            case TOPRIGHT, TOPLEFT-> {
                yValue = 1;
                yield yValue;
            }
            case BOTTOMRIGHT, BOTTOMLEFT-> {
                yValue= -1;
                yield yValue;
            }
            case NONE-> {
                yield 0;
            }
        };
    }

}
