package com.robopoker.gameEngine;

import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 15:29
 */
public class ChipHandler {
    public void makeSmallBlindTransaction(Player player, TableState tableState) {
        final int smallBlindValue = tableState.getSmallBlindValue();
        player.setBetValue(smallBlindValue);
        player.setBalance(player.getBalance() - smallBlindValue);

        player.setStatus(new PlayerAction(PlayerAction.Type.SMALL_BLIND, smallBlindValue));
    }

    public void makeBigBlindTransaction(Player player, TableState tableState) {

    }

    public void makeWantedMove(Player player, TableState tableState) {

    }
}
