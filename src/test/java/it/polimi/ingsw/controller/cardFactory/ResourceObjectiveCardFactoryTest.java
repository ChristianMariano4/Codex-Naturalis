package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceObjectiveCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/resourceObjectiveCards.json";
        ResourceObjectiveCardFactory resourceObjectiveCardFactory = new ResourceObjectiveCardFactory();

        try {
            ArrayList<ObjectiveCard> resourceObjectiveCards = resourceObjectiveCardFactory.createCardList();
            assertNotNull(resourceObjectiveCards);
            assertEquals(95, resourceObjectiveCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

//    @Test
//    public void testCreateCardListWithInvalidFilePath() {
//        String invalidFilePath = "path/to/nonexistent/file.json";
//        ResourceObjectiveCardFactory resourceObjectiveCardFactory = new ResourceObjectiveCardFactory(invalidFilePath);
//
//        assertThrows(CardNotImportedException.class, resourceObjectiveCardFactory::createCardList);
//    }
}