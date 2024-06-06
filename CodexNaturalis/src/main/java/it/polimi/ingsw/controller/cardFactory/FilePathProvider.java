package it.polimi.ingsw.controller.cardFactory;

/**
 * This interface provides the file paths for different types of cards in the game.
 * It is implemented by classes that provide specific file paths for each type of card.
 */
public interface FilePathProvider {

    /**
     * Returns the file path for gold cards.
     *
     * @return A string representing the file path for gold cards.
     */
    String getGoldCardsFilePath();

    /**
     * Returns the file path for resource cards.
     *
     * @return A string representing the file path for resource cards.
     */
    String getResourceCardsFilePath();

    /**
     * Returns the file path for starter cards.
     *
     * @return A string representing the file path for starter cards.
     */
    String getStarterCardsFilePath();

    /**
     * Returns the file path for positional objective cards.
     *
     * @return A string representing the file path for positional objective cards.
     */
    String getPositionalObjectiveCardsFilePath();

    /**
     * Returns the file path for resource objective cards.
     *
     * @return A string representing the file path for resource objective cards.
     */
    String getResourceObjectiveCardsFilePath();

    /**
     * Returns the file path for triple objective cards.
     *
     * @return A string representing the file path for triple objective cards.
     */
    String getTripleObjectiveCardFilePath();
}