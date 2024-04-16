package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.ArrayList;

public abstract class CardFactory<T> {
    public abstract ArrayList<T> createCardList() throws CardNotImportedException;
}
