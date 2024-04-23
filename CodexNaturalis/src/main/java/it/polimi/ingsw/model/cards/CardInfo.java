package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;

import java.util.ArrayList;

public class CardInfo {
    private final CardType cardType;
    //PlayableCard attributes
    private ArrayList<Resource> requirements;
    private GoldPointCondition goldPointCondition;
    //ObjectiveCard attributes
    private Resource cardColor;
    private AngleOrientation orientation;
    private PositionalType positionalType;
    private Resource cardResource;

    /**
     * GoldCard CardInfo constructor
     * @param cardType
     * @param requirements
     * @param goldPointCondition
     */
    public CardInfo(CardType cardType, ArrayList<Resource> requirements, GoldPointCondition goldPointCondition)
    {
        this.cardType = cardType;
        this.requirements = requirements;
        this.goldPointCondition = goldPointCondition;

        this.cardColor = Resource.NONE;
        this.orientation = AngleOrientation.NONE;
        this.positionalType = PositionalType.NONE;
        this.cardResource = Resource.NONE;

    }

    /**
     * ResourceCard and TripleObjectiveCard CardInfo constructor
     * @param cardType
     */
    public CardInfo(CardType cardType)
    {
        this.cardType = cardType;
        this.requirements = new ArrayList<>();
        this.goldPointCondition = GoldPointCondition.NONE;

        this.cardColor = Resource.NONE;
        this.orientation = AngleOrientation.NONE;
        this.positionalType = PositionalType.NONE;
        this.cardResource = Resource.NONE;
    }

    /**
     * ResourceObjectiveCard CardInfo constructor
     * @param cardType
     * @param cardResource
     */
    public CardInfo(CardType cardType, Resource cardResource)
    {
        this.cardType = cardType;
        this.cardResource = cardResource;

        this.requirements = new ArrayList<>();
        this.goldPointCondition = GoldPointCondition.NONE;
        this.cardColor = Resource.NONE;
        this.orientation = AngleOrientation.NONE;
        this.positionalType = PositionalType.NONE;

    }
    public CardInfo(CardType cardType, Resource cardColor, AngleOrientation orientation, PositionalType positionalType)
    {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.orientation = orientation;
        this.positionalType = positionalType;

        this.requirements = new ArrayList<>();
        this.goldPointCondition = GoldPointCondition.NONE;
        this.cardResource = Resource.NONE;
    }


}
