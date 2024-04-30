package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.PlayableCard;

import javax.management.StandardEmitterMBean;

public class PointCalculator {
    public static int calculateTripleObjective(CardInfo cardInfo, PlayerField playerField)
    {

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
    public static int calculatePositionalObjective(CardInfo cardInfo, PlayerField playerField)
    {

    }

}
