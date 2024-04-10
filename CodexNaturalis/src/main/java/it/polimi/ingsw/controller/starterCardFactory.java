package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class starterCardFactory {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try(Reader reader = new FileReader("C:\\Users\\hp\\IdeaProjects\\ing-sw-2024-paganelli-mariano-meacci-martini\\CodexNaturalis\\resources\\starterCards.json")) {
            StarterCard starterCard = gson.fromJson(reader, StarterCard.class);
            System.out.println(starterCard.getAngle(AngleOrientation.TOPRIGHT).getResource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
