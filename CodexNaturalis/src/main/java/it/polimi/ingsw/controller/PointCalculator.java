package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.Collections;
import java.util.HashMap;
import it.polimi.ingsw.model.cards.PlayableCard;

import javax.management.StandardEmitterMBean;

public class PointCalculator {
    /**
     * Calculate all points given by the tripleObjective card
     * @param playerField of the player for the map
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return the total amount of points
     */
    public static int calculateTripleObjective(PlayerField playerField, ObjectiveCard objectiveCard)
    {
        PlayableCard[][] matrixField = playerField.getMatrixField();
        HashMap<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.INKWELL, 0);
        resources.put(Resource.QUILL, 0);
        resources.put(Resource.MANUSCRIPT, 0);

        for (PlayableCard[] playableCards : matrixField) {
            for (int j = 0; j < matrixField[0].length; j++) {
                if (playableCards[j] != null) {
                    for (AngleOrientation orientation : AngleOrientation.values()) //checks all angles
                    {
                        if ((playableCards[j].getAngle(orientation).getAngleStatus() == AngleStatus.OVER || playableCards[j].getAngle(orientation).getAngleStatus() == AngleStatus.OVER) && (playableCards[j].getAngle(orientation).getResource() == Resource.INKWELL || playableCards[j].getAngle(orientation).getResource() == Resource.MANUSCRIPT || playableCards[j].getAngle(orientation).getResource() == Resource.QUILL)) // checks if angle has valid resource needed for TripleObjectiveCard
                        {
                            resources.put(playableCards[j].getAngle(orientation).getResource(), resources.get(playableCards[j].getAngle(orientation).getResource()) + 1); //adds 1 to found resource amount in hashmap
                        }
                    }
                }
            }
        }
        return Collections.min(resources.values())*objectiveCard.getPoints();
    }

    /**
     * Calculate all points given by the objectiveCard
     * @param cardInfo the information of the objectiveCard
     * @param playerField of the player for the map
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return the total amount of points
     */
    public static int calculateResourceObjective(CardInfo cardInfo, PlayerField playerField, ObjectiveCard objectiveCard)
    {
        int points = 0;
        int temp = 0;
        PlayableCard[][] matrixFiled = playerField.getMatrixField();

        for (PlayableCard[] playableCards : matrixFiled) {
            for (int j = 0; j < matrixFiled[0].length; j++) {
                if (playableCards[j] != null) {
                    for (AngleOrientation angleorientation : AngleOrientation.values()) {
                        if (playableCards[j].getAngle(angleorientation).getResource() == cardInfo.getCardResource() && (playableCards[j].getAngle(angleorientation).getAngleStatus() == AngleStatus.OVER || playableCards[j].getAngle(angleorientation).getAngleStatus() == AngleStatus.UNLINKED)) {
                            if (temp == 1) {
                                points += objectiveCard.getPoints();
                                temp = 0;
                            } else {
                                temp++;
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    /**
     * Calculate all points given by the positionalObjectiveCard
     * @param cardInfo the information of the objectiveCard
     * @param playerField of the player for the map
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return the total amount of points
     * @throws CardTypeMismatchException if the cardInfo positionalType doesn't match the DIAGONAL or LSHAPED type
     */
    public static int calculatePositionalObjective(CardInfo cardInfo, PlayerField playerField, ObjectiveCard objectiveCard) throws CardTypeMismatchException {
        PlayableCard[][] matrixField = playerField.getMatrixField();
        return switch (cardInfo.getPositionalType()) {
            case PositionalType.DIAGONAL -> calculateDiagonalObjective(cardInfo, matrixField, objectiveCard);
            case PositionalType.LSHAPED -> calculateLShapedObjective(cardInfo, matrixField, objectiveCard);
            default -> throw new CardTypeMismatchException();
        };
    }

    /**
     * Calculate all points given by the diagonalObjectiveCard
     * @param cardInfo the information of the objectiveCard
     * @param matrixField is the matrix of the field
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return the total amount of points
     */
    private static int calculateDiagonalObjective(CardInfo cardInfo,  PlayableCard[][] matrixField, ObjectiveCard objectiveCard)
    {
        int xValue = cardInfo.getOrientation().mapEnumToX();
        int yValue = cardInfo.getOrientation().mapEnumToY();
        int points = 0;
        Resource objectiveCardColor = cardInfo.getCardColor();
        boolean[][] isVisited = new boolean[matrixField.length][matrixField[0].length]; //should be false by default, check if correct
        for (int i = 0; i < matrixField.length; i++) {
            for (int j = 0; j < matrixField[0].length; j++) {
                if (matrixField[i][j] != null && matrixField[i][j].getCardColor().equals(objectiveCardColor)) {
                    try {
                        int firstNextX = i + xValue;
                        int firstNextY = j + yValue;
                        PlayableCard firstNext = matrixField[firstNextX][firstNextY];
                        int secondNextX = firstNextX + xValue;
                        int secondNextY = firstNextY + yValue;
                        PlayableCard secondNext = matrixField[secondNextX][secondNextY];
                        if(firstNext != null && secondNext != null && !isVisited[i][j] && !isVisited[firstNextX][firstNextY] && !isVisited[secondNextX][secondNextY])
                        {

                            if(firstNext.getCardColor().equals(objectiveCardColor) && secondNext.getCardColor().equals(objectiveCardColor))
                            {
                                points += objectiveCard.getPoints();
                                isVisited[i][j] = true;
                                isVisited[firstNextX][firstNextY] = true;
                                isVisited[secondNextX][secondNextY] = true;
                            }
                        }

                    }
                    catch(IndexOutOfBoundsException e)
                    {
                        continue;
                    }
                }
            }
        }
        return points;
    }

    /**
     * Calculate all points given by the lShapedObjectiveCard
     * @param cardInfo the information of the objectiveCard
     * @param matrixFieled is the matrix of the field
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return he total amount of points
     */
    private static int calculateLShapedObjective(CardInfo cardInfo, PlayableCard[][] matrixFieled, ObjectiveCard objectiveCard)
    {
        int points = 0;
        int xValue = cardInfo.getOrientation().mapEnumToXLShaped();
        int yValue = cardInfo.getOrientation().mapEnumToYLShaped();
        boolean[][] isVisited = new boolean[matrixFieled.length][matrixFieled[0].length];

        for(int i = 0; i < matrixFieled.length; i++) {
            for(int j = 0; j < matrixFieled[0].length; j++) {
                if(matrixFieled[i][j] != null) {
                    if(matrixFieled[i][j].getCardColor() == cardInfo.getCardColor().getOtherLShaped()) {
                        try {
                            int firstNextX = i + xValue;
                            int firstAndSecondNextY = j + yValue;
                            int secondNextX = firstNextX + xValue + xValue; //moves two positions
                            PlayableCard firstNext = matrixFieled[firstNextX][firstAndSecondNextY];
                            PlayableCard secondNext = matrixFieled[secondNextX][firstAndSecondNextY];
                            if(firstNext != null && secondNext != null && !isVisited[firstNextX][firstAndSecondNextY] && !isVisited[secondNextX][firstAndSecondNextY]) {
                                if(firstNext.getCardColor() == cardInfo.getCardColor() && secondNext.getCardColor() == cardInfo.getCardColor()) {
                                    points += objectiveCard.getPoints();
                                    isVisited[firstNextX][firstAndSecondNextY] = true;
                                    isVisited[secondNextX][firstAndSecondNextY] = true;
                                    isVisited[i][j] = true;
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
            }
        }
        return points;
    }

    public static int calculatePlayedCardPoints(Player player, PlayableCard card, CardInfo cardInfo)
    {
        if(cardInfo.getCardType().equals(CardType.RESOURCE))
            return card.getPoints();
        GoldPointCondition condition = cardInfo.getGoldPointCondition();
        switch (cardInfo.getGoldPointCondition())
        {
            case null -> {
                return card.getPoints();
            }
            case GoldPointCondition.NONE ->
            {
                return card.getPoints();
            }
            case ANGLE ->
            {
                PlayableCard[][] playerField = player.getPlayerField().getMatrixField();
                for(int i = 0; i < GameValues.DEFAULT_MATRIX_SIZE; i++)
                {
                    for(int j = 0; j < GameValues.DEFAULT_MATRIX_SIZE; j++)
                    {
                        if(playerField[i][j] != null && playerField[i][j].equals(card))
                        {
                            int angles = 0;
                            for(AngleOrientation orientation : AngleOrientation.values())
                            {
                                if(orientation.equals(AngleOrientation.NONE))
                                    continue;
                                if(playerField[i + orientation.mapEnumToX()][j + orientation.mapEnumToY()] != null)
                                {
                                    angles += 1;
                                }
                            }
                            return angles * card.getPoints();
                        }
                    }

                }
                throw new RuntimeException();
            }
            //INKWELL, QUILL AND MANUSCRIPT
            default ->
            {
                return (player.getResourceAmount(condition.mapToResource()) + 1)* card.getPoints();
                //adding 1 to include the resource on the card being played, as per rules
            }
        }

    }

}
