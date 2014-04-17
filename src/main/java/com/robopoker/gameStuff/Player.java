package com.robopoker.gameStuff;

import java.util.List;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 13:18
 */
public class Player {
    private PlayerAction status;
    private List<Card> playerCards;

    private int betValue;

    private PlayerAction wantedMove;
    private int balance;

    public PlayerAction getWantedMove() {
        return wantedMove;
    }

    public void setWantedMove(PlayerAction wantedMove) {
        this.wantedMove = wantedMove;
    }

    public PlayerAction getStatus() {
        return status;
    }

    public void setStatus(PlayerAction status) {
        this.status = status;
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public int getBetValue() {
        return betValue;
    }

    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
