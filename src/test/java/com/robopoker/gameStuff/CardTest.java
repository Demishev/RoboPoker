package com.robopoker.gameStuff;

import org.junit.Test;

import static com.robopoker.gameStuff.CardSuit.DIAMONDS;
import static com.robopoker.gameStuff.CardSuit.HEARTS;
import static com.robopoker.gameStuff.CardValue.*;
import static junit.framework.Assert.*;

/**
 * User: Konstantin Demishev
 * Date: 02.12.12
 * Time: 20:51
 */
public class CardTest {

    @Test
    public void shouldHeartsAceToStringProperlyWhenCreatingNewCard() throws Exception {
        assertEquals("A♥", new Card(HEARTS, ACE).toString());
    }

    @Test
    public void shouldDiamondKingIsNearDiamondAce() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).isNear(new Card(DIAMONDS, ACE)));
    }

    @Test
    public void shouldDiamondKingIsNotNearDiamondJack() throws Exception {
        assertFalse(new Card(DIAMONDS, KING).isNear(new Card(DIAMONDS, JACK)));
    }

    @Test
    public void shouldDiamondKingIsNearHeartAce() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).isNear(new Card(HEARTS, ACE)));
    }

    @Test
    public void shouldSameValueWhenDiamondKingDiamondKing() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).sameValue(new Card(DIAMONDS, KING)));
    }

    @Test
    public void shouldSameValueWhenDiamondKingHeartKing() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).sameValue(new Card(HEARTS, KING)));
    }

    @Test
    public void shouldNotSameValueWhenDiamondJackDiamondKing() throws Exception {
        assertFalse(new Card(DIAMONDS, KING).sameValue(new Card(DIAMONDS, JACK)));
    }

    @Test
    public void shouldSameSuitWhenDiamondKingDiamondKing() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).sameSuit(new Card(DIAMONDS, KING)));
    }

    @Test
    public void shouldSameSuitWhenDiamondKingDiamondJack() throws Exception {
        assertTrue(new Card(DIAMONDS, KING).sameSuit(new Card(DIAMONDS, JACK)));
    }

    @Test
    public void shouldNotSameSuitWhenDiamondKingHeartsKing() throws Exception {
        assertFalse(new Card(DIAMONDS, KING).sameSuit(new Card(HEARTS, KING)));
    }

    @Test
    public void shouldKingWhenGetCardFullValueNameDiamondKing() throws Exception {
        assertEquals("King", new Card(DIAMONDS, KING).getCardValueName());
    }

    @Test
    public void shouldQueenWhenGetCardFullValueNameDiamondQueen() throws Exception {
        assertEquals("Queen", new Card(DIAMONDS, QUEEN).getCardValueName());
    }
}
