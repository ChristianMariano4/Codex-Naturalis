package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.Serializable;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
/**
 * This class represents a player field. It contains the field of the player, represented by a matrix.
 * This class is responsible for adding cards to the player field.
 */
public class PlayerField implements Serializable {
    private final PlayableCard[][] matrixField;

    /**
     * Constructor
     */
    public PlayerField()
    {
        this.matrixField = new PlayableCard[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
    }

    /**
     * Getter
     * @return the MatrixFiled of the player
     */
    public PlayableCard[][] getMatrixField()
    {
        PlayableCard[][] returnedMatrixField = new PlayableCard[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(int i = 0; i<DEFAULT_MATRIX_SIZE; i++)
        {
            System.arraycopy(matrixField[i], 0, returnedMatrixField[i], 0, DEFAULT_MATRIX_SIZE);
        }
        return returnedMatrixField;
    }

    //TODO: dynamic resizing

    /**
     * Add a card to the player field
     * @param card is the reference to the selected card
     * @param angleOrientation indicate the selected angle
     * @param cardToAdd is the reference to the card to add to the playerField
     * @throws InvalidCardPositionException when the player try to add a card in an invalid position
     */
    public void addCardToCell(PlayableCard card, AngleOrientation angleOrientation, PlayableCard cardToAdd) throws InvalidCardPositionException
    {
        try{
            for(int i = 0; i<DEFAULT_MATRIX_SIZE; i++)
            {
                for(int j = 0; j<DEFAULT_MATRIX_SIZE; j++)
                {
                    if(card.equals(matrixField[i][j]))
                    {
                        int cardToAddX = i + angleOrientation.mapEnumToX();
                        int cardToAddY = j + angleOrientation.mapEnumToY();
                        if(matrixField[cardToAddX][cardToAddY] != null)
                            throw new InvalidCardPositionException();
                        matrixField[cardToAddX][cardToAddY] = cardToAdd;
                        i = DEFAULT_MATRIX_SIZE;
                        break;

                    }
                }
            }
            throw new InvalidCardPositionException();
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new InvalidCardPositionException();
        }
    }

    public void addCardToCell(PlayableCard starterCard){
        matrixField[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2] = starterCard;
    }

}
