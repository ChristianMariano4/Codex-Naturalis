package it.polimi.ingsw.controller.cardFactory;

/**
 * This class provides the file paths for different types of cards in the game.
 * It implements the FilePathProvider interface.
 */
public class ProductionFilePathProvider implements FilePathProvider {

    /**
     * Returns the file path for gold cards.
     *
     * @return A string representing the file path for gold cards.
     */
    @Override
    public String getGoldCardsFilePath() {
        return "CodexNaturalis/src/main/resources/goldCards.json";
    }

    /**
     * Returns the file path for resource cards.
     *
     * @return A string representing the file path for resource cards.
     */
    @Override
    public String getResourceCardsFilePath() {
        return "CodexNaturalis/src/main/resources/resourceCards.json";
    }

    /**
     * Returns the file path for starter cards.
     *
     * @return A string representing the file path for starter cards.
     */
    @Override
    public String getStarterCardsFilePath() {
        return "CodexNaturalis/src/main/resources/starterCards.json";
    }

    /**
     * Returns the file path for positional objective cards.
     *
     * @return A string representing the file path for positional objective cards.
     */
    @Override
    public String getPositionalObjectiveCardsFilePath() {
        return "CodexNaturalis/src/main/resources/positionalObjectiveCards.json";
    }

    /**
     * Returns the file path for resource objective cards.
     *
     * @return A string representing the file path for resource objective cards.
     */
    @Override
    public String getResourceObjectiveCardsFilePath() {
        return "CodexNaturalis/src/main/resources/resourceObjectiveCards.json";
    }

    /**
     * Returns the file path for triple objective cards.
     *
     * @return A string representing the file path for triple objective cards.
     */
    @Override
    public String getTripleObjectiveCardFilePath() {
        return "CodexNaturalis/src/main/resources/tripleObjectiveCard.json";
    }
}