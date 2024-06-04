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

import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import it.polimi.ingsw.model.cards.PlayableCard;

import javax.management.StandardEmitterMBean;

public class PointCalculator {
    /**
     * Calculate all points given by the tripleObjective card
     * @param player the player
     * @return the total amount of points
     */
    public static int calculateTripleObjective(Player player, ObjectiveCard objectiveCard)
    {
        ArrayList<Integer> resourceAmounts = new ArrayList<>();
        resourceAmounts.add(player.getResourceAmount(Resource.INKWELL));
        resourceAmounts.add(player.getResourceAmount(Resource.MANUSCRIPT));
        resourceAmounts.add(player.getResourceAmount(Resource.QUILL));
        return Collections.min(resourceAmounts) * objectiveCard.getPoints();
    }

    /**
     * Calculate all points given by the objectiveCard
     * @param cardInfo the information of the objectiveCard
     * @param player the player
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return the total amount of points
     */
    public static int calculateResourceObjective(CardInfo cardInfo, Player player, ObjectiveCard objectiveCard)
    {
        int requiredNumber = switch (cardInfo.getCardResource()) {
            case MANUSCRIPT, QUILL, INKWELL -> 2;
            default -> 3;
        };

        return (player.getResourceAmount(cardInfo.getCardResource())/requiredNumber) * objectiveCard.getPoints();
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
     * @param matrixField is the matrix of the field
     * @param objectiveCard the objectiveCard used to calculate the points
     * @return he total amount of points
     */
    private static int calculateLShapedObjective(CardInfo cardInfo, PlayableCard[][] matrixField, ObjectiveCard objectiveCard)
    {
        int points = 0;
        int xValue = cardInfo.getOrientation().mapEnumToXLShaped();
        int yValue = cardInfo.getOrientation().mapEnumToYLShaped();
        boolean[][] isVisited = new boolean[matrixField.length][matrixField[0].length];

        for(int i = 0; i < matrixField.length; i++) {
            for(int j = 0; j < matrixField[0].length; j++) {
                if(matrixField[i][j] != null) {
                    if(matrixField[i][j].getCardColor().equals(cardInfo.getCardColor().getOtherLShaped())) {
                        try {
                            int firstNextX = i + xValue;
                            int firstAndSecondNextY = j + yValue;
                            int secondNextX = firstNextX + xValue + xValue; //moves two positions
                            PlayableCard firstNext = matrixField[firstNextX][firstAndSecondNextY];
                            PlayableCard secondNext = matrixField[secondNextX][firstAndSecondNextY];
                            if(firstNext != null && secondNext != null && !isVisited[firstNextX][firstAndSecondNextY] && !isVisited[secondNextX][firstAndSecondNextY]) {
                                if(firstNext.getCardColor().equals(cardInfo.getCardColor()) && secondNext.getCardColor().equals(cardInfo.getCardColor())) {
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

    /**
     * Calculate the points given by the playedCard
     * @param player the player that played the card
     * @param card the card played
     * @param cardInfo the information of the card
     * @return the total amount of points given by the card
     */
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
