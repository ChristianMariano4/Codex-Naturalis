package it.polimi.ingsw.controller.cardFactory;

public class ProductionFilePathProvider implements FilePathProvider {
    @Override
    public String getGoldCardsFilePath() {
        return "CodexNaturalis/src/main/resources/goldCards.json";
    }

    @Override
    public String getResourceCardsFilePath() {
        return "CodexNaturalis/src/main/resources/resourceCards.json";
    }

    @Override
    public String getStarterCardsFilePath() {
        return "CodexNaturalis/src/main/resources/starterCards.json";
    }

    @Override
    public String getPositionalObjectiveCardsFilePath() {
        return "CodexNaturalis/src/main/resources/positionalObjectiveCards.json";
    }

    @Override
    public String getResourceObjectiveCardsFilePath() {
        return "CodexNaturalis/src/main/resources/resourceObjectiveCards.json";
    }

    @Override
    public String getTripleObjectiveCardFilePath() {
        return "CodexNaturalis/src/main/resources/tripleObjectiveCard.json";
    }
}
