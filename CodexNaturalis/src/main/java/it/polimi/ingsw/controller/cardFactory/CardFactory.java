package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;

import java.util.ArrayList;

public abstract class CardFactory<T> {
    public abstract ArrayList<T> createCardList() throws CardNotImportedException;
}
