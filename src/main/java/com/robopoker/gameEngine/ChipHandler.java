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
        final int wantedBet = tableState.getSmallBlindValue();
        final PlayerAction.Type wantedStatus = PlayerAction.Type.SMALL_BLIND;

        makeChipTransaction(player, wantedStatus, wantedBet);
    }

    public void makeBigBlindTransaction(Player player, TableState tableState) {
        final int wantedBet = 2 * tableState.getSmallBlindValue();
        final PlayerAction.Type wantedStatus = PlayerAction.Type.BIG_BLIND;

        makeChipTransaction(player, wantedStatus, wantedBet);
    }

    public void makeWantedMove(Player player, TableState tableState) {
        makeChipTransaction(player, PlayerAction.Type.ALL_IN, player.getBalance());
    }

    private void makeChipTransaction(Player player, PlayerAction.Type wantedStatus, int wantedBet) {
        final int balance = player.getBalance();
        if (balance > wantedBet) {
            player.setStatus(new PlayerAction(wantedStatus, wantedBet));
            player.setBalance(balance - wantedBet);
            player.setBetValue(wantedBet);
        } else {
            player.setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, balance));
            player.setBetValue(balance);
            player.setBalance(0);
        }
    }
}
