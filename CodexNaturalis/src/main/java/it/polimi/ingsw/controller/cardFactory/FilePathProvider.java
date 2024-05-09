package it.polimi.ingsw.controller.cardFactory;

public interface FilePathProvider {
    String getGoldCardsFilePath();
    String getResourceCardsFilePath();
    String getStarterCardsFilePath();
    String getPositionalObjectiveCardsFilePath();
    String getResourceObjectiveCardsFilePath();
    String getTripleObjectiveCardFilePath();
}