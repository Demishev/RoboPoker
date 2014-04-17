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
        final int balance = player.getBalance();

        if (balance > smallBlindValue) {
            player.setStatus(new PlayerAction(PlayerAction.Type.SMALL_BLIND, smallBlindValue));
            player.setBalance(balance - smallBlindValue);
            player.setBetValue(smallBlindValue);
        } else {
            player.setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, balance));
            player.setBetValue(balance);
            player.setBalance(0);
        }
    }

    public void makeBigBlindTransaction(Player player, TableState tableState) {

    }

    public void makeWantedMove(Player player, TableState tableState) {

    }
}
