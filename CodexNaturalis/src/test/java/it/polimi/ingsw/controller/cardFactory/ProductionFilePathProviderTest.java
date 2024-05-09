package it.polimi.ingsw.controller.cardFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductionFilePathProviderTest {

    private final ProductionFilePathProvider filePathProvider = new ProductionFilePathProvider();

    @Test
    void getGoldCardsFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/goldCards.json", filePathProvider.getGoldCardsFilePath());
    }

    @Test
    void getResourceCardsFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/resourceCards.json", filePathProvider.getResourceCardsFilePath());
    }

    @Test
    void getStarterCardsFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/starterCards.json", filePathProvider.getStarterCardsFilePath());
    }

    @Test
    void getPositionalObjectiveCardsFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/positionalObjectiveCards.json", filePathProvider.getPositionalObjectiveCardsFilePath());
    }

    @Test
    void getResourceObjectiveCardsFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/resourceObjectiveCards.json", filePathProvider.getResourceObjectiveCardsFilePath());
    }

    @Test
    void getTripleObjectiveCardFilePath_returnsCorrectPath() {
        assertEquals("CodexNaturalis/src/main/resources/tripleObjectiveCard.json", filePathProvider.getTripleObjectiveCardFilePath());
    }
}
