package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TableTop class is the class that contains the drawing field and all the players' fields.
 * It is responsible for adding players to the game and managing the drawing field.
 */
public class TableTop implements Serializable {
    private final DrawingField drawingField;
    private final ArrayList<ObjectiveCard> sharedObjectiveCards;

    /**
     * Constructor
     * @param drawingField is the reference to the drawingField
     * @throws InvalidConstructorDataException when controller didn't properly create TableTop
     */
    public TableTop(DrawingField drawingField, ArrayList<ObjectiveCard> sharedObjectiveCards) throws InvalidConstructorDataException {
        try {
            this.drawingField = drawingField;
            this.sharedObjectiveCards = new ArrayList<>(sharedObjectiveCards);
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     * Get the drawing field of the game
     * @return the reference to the drawingField
     */
    public DrawingField getDrawingField() {
        return drawingField;
    }


    /**
     * Get the two shared objective cards of the game
     * @return the shared objective cards
     */
    public ArrayList<ObjectiveCard> getSharedObjectiveCards(){
        return sharedObjectiveCards;
    }



}
