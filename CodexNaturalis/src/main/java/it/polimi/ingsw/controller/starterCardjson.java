package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.Angle;
import it.polimi.ingsw.model.cards.StarterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class starterCardjson {
    public static void main(String[] args) throws InvalidConstructorDataException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        StarterCard starterCard = createStarterCard();
        String json = gson.toJson(starterCard);
        System.out.println(json);

    }

    private static StarterCard createStarterCard() throws InvalidConstructorDataException {
        List<Resource> resources = new ArrayList<>(Resource.INSECT.ordinal());
        Map<AngleOrientation,Angle> angles = new HashMap<>();
        StarterCard starterCard = new StarterCard(1, it.polimi.ingsw.enumerations.Side.FRONT, resources, angles, Resource.ANIMAL, 1);
        return starterCard;
    }
}
