package it.polimi.ingsw.controller.cardFactory;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.exceptions.CardNotImportedException;

import java.util.ArrayList;

public class PositionalObjectiveCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/PositionalObjectiveCards.json";
        PositionalObjectiveCardFactory positionalObjectiveCardFactory = new PositionalObjectiveCardFactory();

        try {
            ArrayList<ObjectiveCard> positionalObjectiveCards = positionalObjectiveCardFactory.createCardList();
            assertNotNull(positionalObjectiveCards);
            assertEquals(87, positionalObjectiveCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

//    @Test
//    public void testCreateCardListWithInvalidFilePath() {
//        String invalidFilePath = "path/to/nonexistent/file.json";
//        PositionalObjectiveCardFactory positionalObjectiveCardFactory = new PositionalObjectiveCardFactory(invalidFilePath);
//        assertThrows(CardNotImportedException.class, positionalObjectiveCardFactory::createCardList);
//    }
}