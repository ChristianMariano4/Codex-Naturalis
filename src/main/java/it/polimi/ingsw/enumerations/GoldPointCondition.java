package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the conditions of the gold cards.
 */
public enum GoldPointCondition {
    NONE,
    ANGLE,
    QUILL,
    INKWELL,
    MANUSCRIPT;

    /**
     * This method returns the string representation of the enumeration.
     * @return the string representation of the enumeration
     */
    public String printResource() {
        return switch (this){
            case NONE -> "  ";
            case ANGLE -> "   ";
            case QUILL -> "QUI";
            case INKWELL -> "INK";
            case MANUSCRIPT -> "MAN";
        };
    }

    /**
     * This method returns the resource associated with the enumeration.
     * @return the resource associated with the enumeration
     */
    public Resource mapToResource()
    {
        switch (this)
        {
            case QUILL ->
            {
                return Resource.QUILL;
            }
            case INKWELL ->
            {
                return Resource.INKWELL;
            }
            case MANUSCRIPT ->
            {
                return Resource.MANUSCRIPT;
            }
            default ->
            {
                return Resource.NONE;
            }
        }
    }
}
