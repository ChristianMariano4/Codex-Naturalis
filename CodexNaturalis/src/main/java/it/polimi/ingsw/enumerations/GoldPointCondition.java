package it.polimi.ingsw.enumerations;

public enum GoldPointCondition {
    NONE,
    ANGLE,
    QUILL,
    INKWELL,
    MANUSCRIPT;

    public String printResource() {
        return switch (this){
            case NONE -> "  ";
            case ANGLE -> "   ";
            case QUILL -> "QUI";
            case INKWELL -> "INK";
            case MANUSCRIPT -> "MAN";
        };
    }
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
