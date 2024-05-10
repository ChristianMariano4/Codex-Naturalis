package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.StarterCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StarterCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/starterCards.json";
        StarterCardFactory starterCardFactory = new StarterCardFactory();

        try {
            ArrayList<StarterCard> starterCards = starterCardFactory.createCardList();
            assertNotNull(starterCards);
            assertEquals(81, starterCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

//    @Test
//    public void testCreateCardListWithInvalidFilePath() {
//        String invalidFilePath = "path/to/nonexistent/file.json";
//        StarterCardFactory starterCardFactory = new StarterCardFactory(invalidFilePath);
//
//        assertThrows(CardNotImportedException.class, starterCardFactory::createCardList);
//    }
}