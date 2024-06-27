package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the orientation of an angle.
 * It is used to determine the angle you want to refer to.
 */
public enum AngleOrientation {
    TOPRIGHT,
    TOPLEFT,
    BOTTOMRIGHT,
    BOTTOMLEFT,
    NONE;

    /**
     * This method maps the enum to the x coordinate.
     * @return the x coordinate
     */
    public int mapEnumToX()
    {
        return switch (this) {
            case BOTTOMLEFT, BOTTOMRIGHT -> 1;
            case TOPLEFT, TOPRIGHT -> -1;
            case NONE -> 0;
        };
    }

    /**
     * This method maps the enum to the y coordinate.
     * @return the y coordinate
     */
    public int mapEnumToY()
    {

        return switch (this) {
            case TOPRIGHT, BOTTOMRIGHT -> 1;
            case TOPLEFT, BOTTOMLEFT -> -1;
            case NONE -> 0;
        };
    }

    /**
     * This method maps the enum to the x coordinate of an L-shaped pattern.
     * @return the x coordinate
     */
    public int mapEnumToXLShaped()
    {
        int xValue;
        return switch(this)
        {
            case TOPRIGHT, TOPLEFT-> {
                xValue = 1;
                yield xValue;
            }
            case BOTTOMRIGHT, BOTTOMLEFT-> {
                xValue= -1;
                yield xValue;
            }
            case NONE-> 0;
        };

    }

    /**
     * This method maps the enum to the y coordinate of an L-shaped pattern.
     * @return the y coordinate
     */
    public int mapEnumToYLShaped()
    {
        int yValue;
        return switch (this) {
            case TOPRIGHT, BOTTOMLEFT -> {
                yValue = 1;
                yield yValue;
            }
            case TOPLEFT, BOTTOMRIGHT -> {
                yValue = -1;
                yield yValue;
            }
            case NONE -> 0;
        };

    }

    /**
     * This method returns the opposite orientation.
     * @return the opposite orientation
     */
    public AngleOrientation getOpposite()
    {
        switch(this){
            case TOPRIGHT -> {
                return BOTTOMLEFT;
            }
            case BOTTOMLEFT -> {
                return TOPRIGHT;
            }
            case TOPLEFT -> {
                return BOTTOMRIGHT;
            }
            case BOTTOMRIGHT -> {
                return TOPLEFT;
            }
            case NONE -> {
                return NONE;
            }
        }
        return NONE;
    }

}
