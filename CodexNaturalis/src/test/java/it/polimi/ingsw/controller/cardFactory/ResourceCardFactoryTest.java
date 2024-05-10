package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.ResourceCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/resourceCards.json";
        ResourceCardFactory resourceCardFactory = new ResourceCardFactory();

        try {
            ArrayList<ResourceCard> resourceCards = resourceCardFactory.createCardList();
            assertNotNull(resourceCards);
            assertEquals(1, resourceCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

//    @Test
//    public void testCreateCardListWithInvalidFilePath() {
//        String invalidFilePath = "path/to/nonexistent/file.json";
//        ResourceCardFactory resourceCardFactory = new ResourceCardFactory(invalidFilePath);
//
//        assertThrows(CardNotImportedException.class, resourceCardFactory::createCardList);
//    }
}