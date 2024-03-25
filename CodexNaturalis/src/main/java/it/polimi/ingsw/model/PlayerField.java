package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.exceptions.InvalidCardPosition;
import it.polimi.ingsw.model.cards.CardWithAngles;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

public class PlayerField {
    private CardWithAngles[][] matrixField;
    public PlayerField()
    {
        this.matrixField = new CardWithAngles[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
    }
    public CardWithAngles[][] getMatrixField()
    {
        CardWithAngles[][] returnedMatrixField = new CardWithAngles[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(int i = 0; i<DEFAULT_MATRIX_SIZE; i++)
        {
            System.arraycopy(matrixField[i], 0, returnedMatrixField[i], 0, DEFAULT_MATRIX_SIZE);
        }
        return returnedMatrixField;
    }

    //TODO: dynamic resizing
    public void addCardToCell(CardWithAngles card, AngleOrientation angleOrientation, CardWithAngles cardToAdd) throws InvalidCardPosition
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
                            throw new InvalidCardPosition();
                        matrixField[cardToAddX][cardToAddY] = cardToAdd;
                        break;
                    }
                }
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new InvalidCardPosition();
        }
    }

}
