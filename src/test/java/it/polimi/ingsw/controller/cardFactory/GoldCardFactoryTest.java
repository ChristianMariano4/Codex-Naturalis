package it.polimi.ingsw.controller.cardFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.model.cards.GoldCard;

import java.util.ArrayList;

public class GoldCardFactoryTest {

    @Test
    public void testCreateCardList() {
        String testFilePath = "src/main/resources/goldCards.json";
        GoldCardFactory goldCardFactory = new GoldCardFactory();

        try {
            ArrayList<GoldCard> goldCards = goldCardFactory.createCardList();
            assertNotNull(goldCards);
            assertEquals(41, goldCards.getFirst().getCardId());
        } catch (CardNotImportedException e) {
            fail("CardNotImportedException was thrown");
        }
    }

//    @Test
//    public void testCreateCardListWithInvalidFilePath() {
//        String invalidFilePath = "path/to/nonexistent/file.json";
//        GoldCardFactory goldCardFactory = new GoldCardFactory(invalidFilePath);
//
//        assertThrows(CardNotImportedException.class, goldCardFactory::createCardList);
//    }
}