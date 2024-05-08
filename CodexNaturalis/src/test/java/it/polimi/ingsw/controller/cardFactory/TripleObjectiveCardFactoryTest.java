package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TripleObjectiveCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/tripleObjectiveCard.json";
        TripleObjectiveCardFactory tripleObjectiveCardFactory = new TripleObjectiveCardFactory(testFilePath);

        try {
            ArrayList<ObjectiveCard> tripleObjectiveCards = tripleObjectiveCardFactory.createCardList();
            assertNotNull(tripleObjectiveCards);
            assertEquals(99, tripleObjectiveCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

    @Test
    public void testCreateCardListWithInvalidFilePath() {
        String invalidFilePath = "path/to/nonexistent/file.json";
        TripleObjectiveCardFactory tripleObjectiveCardFactory = new TripleObjectiveCardFactory(invalidFilePath);

        assertThrows(CardNotImportedException.class, tripleObjectiveCardFactory::createCardList);
    }
}