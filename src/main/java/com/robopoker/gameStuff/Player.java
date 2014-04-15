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

    public PlayerAction getStatus() {
        return status;
    }

    public void setStatus(PlayerAction status) {
        this.status = status;
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }
}
