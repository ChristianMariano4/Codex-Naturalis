package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.Collections;
import java.util.HashMap;
import it.polimi.ingsw.model.cards.PlayableCard;

import javax.management.StandardEmitterMBean;

public class PointCalculator {
    public static int calculateTripleObjective(CardInfo cardInfo, PlayerField playerField)
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
        return Collections.min(resources.values());
    }
    public static int calculateResourceObjective(CardInfo cardInfo, PlayerField playerField)
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
                                points++;
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
    public static int calculatePositionalObjective(CardInfo cardInfo, PlayerField playerField) throws CardTypeMismatchException {
        PlayableCard[][] matrixFiled = playerField.getMatrixField();
        return switch (cardInfo.getPositionalType()) {
            case PositionalType.DIAGONAL -> calculateDiagonalObjective(cardInfo, matrixFiled);
            case PositionalType.LSHAPED -> calculateLShapedObjective(cardInfo, matrixFiled);
            default -> throw new CardTypeMismatchException();
        };
    }
    private static int calculateDiagonalObjective(CardInfo cardInfo,  PlayableCard[][] matrixFiled)
    {

    }
    private static int calculateLShapedObjective(CardInfo cardInfo, PlayableCard[][] matrixFieled, ObjectiveCard objectiveCard)
    {
        int points = 0;
        int xValue = cardInfo.getOrientation().mapEnumToXLShaped();
        int yValue = cardInfo.getOrientation().mapEnumToYLShaped();
        boolean[][] isVisited = new boolean[matrixFieled.length][matrixFieled[0].length];

        for(int i = 0; i < matrixFieled.length; i++) {
            for(int j = 0; j < matrixFieled[0].length; j++) {
                if(matrixFieled[i][j] != null) {
                    if(matrixFieled[i][j].getCardColor() == cardInfo.getCardColor()) {
                        try {
                            int firstAndSecondNextX = i + xValue;
                            int firstNextY = j + yValue;
                            int secondNextY = firstNextY + yValue;
                            PlayableCard firstNext = matrixFieled[firstAndSecondNextX][firstNextY];
                            PlayableCard secondNext = matrixFieled[firstAndSecondNextX][secondNextY];
                            if(firstNext != null && secondNext != null && !isVisited[firstAndSecondNextX][firstNextY] && !isVisited[firstAndSecondNextX][secondNextY]) {
                                if(firstNext.getCardColor() == cardInfo.getCardColor() && secondNext.getCardColor() == cardInfo.getCardColor()) {
                                    points += objectiveCard.getPoints();
                                    isVisited[firstAndSecondNextX][firstNextY] = true;
                                    isVisited[firstAndSecondNextX][secondNextY] = true;
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
}
