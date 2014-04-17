package com.robopoker.gameEngine;

import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

import java.util.List;

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
        switch (player.getWantedMove().getType()) {
            case ALL_IN:
                makeChipTransaction(player, PlayerAction.Type.ALL_IN, player.getBalance());
                break;
            case BET:
                final int bet = defineBetValue(player, tableState);

                makeChipTransaction(player, PlayerAction.Type.BET, bet);
                break;
        }
    }

    private int defineBetValue(Player player, TableState tableState) {
        final int minBet;
        final GameStage gameStage = tableState.getGameStage();
        if (gameStage == GameStage.PREFLOP || gameStage == GameStage.FLOP) {
            minBet = tableState.getSmallBlindValue() * 2;
        } else {
            minBet = tableState.getSmallBlindValue() * 4;
        }
        List<Player> players = tableState.getPlayers();
        final int callValue = players.stream().max((o1, o2) -> o1.getBetValue() - o2.getBetValue()).get().getBetValue();

        final int wantedValue = player.getWantedMove().getValue();

        if (callValue >= minBet && callValue >= wantedValue) {
            return callValue;
        }
        if (minBet >= callValue && minBet >= wantedValue) {
            return minBet;
        }
        return wantedValue;
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
