package it.polimi.ingsw.controller.cardFactory;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PositionalObjectiveCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GoldCardFactory extends CardFactory<GoldCard> {


    public ArrayList<GoldCard> createCardList() throws CardNotImportedException {
        GoldCard[] goldCardCardArray;
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("/goldCards.json"))) {
            goldCardCardArray = gson.fromJson(reader, GoldCard[].class);
        } catch (IOException e) {
            throw new CardNotImportedException();
        }
        return new ArrayList<>(Arrays.asList(goldCardCardArray));
    }
}