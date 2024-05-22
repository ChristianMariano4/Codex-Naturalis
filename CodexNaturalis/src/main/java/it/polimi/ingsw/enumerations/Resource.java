package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.model.Player;

public enum Resource {
    ANIMAL,
    INSECT,
    PLANT,
    FUNGI,
    QUILL,
    INKWELL,
    MANUSCRIPT,
    NONE;

    /**
     * Used when calculating points for PositionalObjectiveCard, returns the resource not saved in the card
     * The two resources are the ones saved in the cardColor field, and the third one is returned by this method
     * @return the third resource
     */
    public Resource getOtherLShaped()
    {
        return switch (this) {
            case FUNGI -> PLANT;
            case PLANT -> INSECT;
            case ANIMAL -> FUNGI;
            case INSECT -> ANIMAL;
            default -> NONE;
        };
    }

    public String printResourceInfo(){
        return switch (this) {
            case FUNGI ->"FUN";
            case ANIMAL -> "ANI";
            case PLANT -> "PLA";
            case INSECT -> "INS";
            case QUILL -> "QUI";
            case INKWELL -> "INK";
            case MANUSCRIPT -> "MAN";
            case NONE -> "   ";
        };
    }
}
