package com.robopoker.gameEngine;

import com.robopoker.gameEngine.gamecommand.GameCommand;
import com.robopoker.gameStuff.Card;
import com.robopoker.gameStuff.CardDeck;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Demishev
 * Date: 14.04.2014
 * Time: 14:23
 */
public class TableState {
    private GameStage gameStage;
    private int dealerNumber;
    private List<Player> players;
    private int lastMovedPlayerNumber;
    private List<Card> deskCards;
    private CardDeck cardDeck;
    private int pot;

    public int getLastMovedPlayerNumber() {
        return lastMovedPlayerNumber;
    }

    public void setLastMovedPlayerNumber(int lastMovedPlayerNumber) {
        this.lastMovedPlayerNumber = lastMovedPlayerNumber;
    }

    public ArrayList<GameCommand> getCommands() {
        return null;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public int getDealerNumber() {
        return dealerNumber;
    }

    public void setDealerNumber(int dealerNumber) {
        this.dealerNumber = dealerNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setCardDeck(CardDeck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public void setDeskCards(List<Card> deskCards) {
        this.deskCards = deskCards;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }
}
