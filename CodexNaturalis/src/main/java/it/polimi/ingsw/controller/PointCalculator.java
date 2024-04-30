package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.CardInfo;
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
    public static int calculatePositionalObjective(CardInfo cardInfo, PlayerField playerField)
    {

    }

}
