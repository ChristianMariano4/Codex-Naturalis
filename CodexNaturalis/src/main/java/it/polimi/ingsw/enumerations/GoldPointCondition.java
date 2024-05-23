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
}
