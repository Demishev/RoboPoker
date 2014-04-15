package com.robopoker.gameStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: Konstantin Demishev
 * Date: 22.11.12
 * Time: 14:08
 */
public class CardDeck {
    private final List<Card> cards = new ArrayList<>();
    private final Random random;

    public CardDeck() {
        random = new Random();
    }

    CardDeck(int randomSeed) {
        random = new Random(randomSeed);
        generateCards();
    }

    private void generateCards() {
        for (CardValue cardValue : CardValue.values()) {
            for (CardSuit cardSuit : CardSuit.values()) {
                cards.add(new Card(cardSuit, cardValue));
            }
        }
    }

    public Card getCard() {
        return cards.remove(random.nextInt(cards.size()));
    }
}
