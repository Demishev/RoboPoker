package com.robopoker.gameEngine;

import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 15:29
 */
public class ChipHandler {

    public static final PlayerAction FOLD = new PlayerAction(PlayerAction.Type.FOLD);

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
            case RISE:
                final int minimumRiseValue = findMaxBet(tableState.getPlayers()) + defineMinBet(tableState);
                final int wantedRiseValue = player.getWantedMove().getValue();

                makeChipTransaction(player, PlayerAction.Type.RISE, Math.max(minimumRiseValue, wantedRiseValue));
                break;
            case FOLD:
                player.setStatus(FOLD);
                break;
            case CALL:
                makeChipTransaction(player, PlayerAction.Type.CALL, findMaxBet(tableState.getPlayers()));
                break;
            case CHECK:
                if (findMaxBet(tableState.getPlayers()) == 0) {
                    player.setStatus(new PlayerAction(PlayerAction.Type.CHECK));
                } else {
                    player.setStatus(FOLD);
                }
        }
    }

    private int defineBetValue(Player player, TableState tableState) {
        final int minBet = defineMinBet(tableState);
        List<Player> players = tableState.getPlayers();
        final int callValue = findMaxBet(players);

        final int wantedValue = player.getWantedMove().getValue();

        if (callValue >= minBet && callValue >= wantedValue) {
            return callValue;
        }
        if (minBet >= callValue && minBet >= wantedValue) {
            return minBet;
        }
        return wantedValue;
    }

    private int defineMinBet(TableState tableState) {
        int minBet;
        final GameStage gameStage = tableState.getGameStage();
        if (gameStage == GameStage.PREFLOP || gameStage == GameStage.FLOP) {
            minBet = tableState.getSmallBlindValue() * 2;
        } else {
            minBet = tableState.getSmallBlindValue() * 4;
        }
        return minBet;
    }

    public int findMaxBet(List<Player> players) {
        return players.stream().max((o1, o2) -> o1.getBetValue() - o2.getBetValue()).get().getBetValue();
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

    public void moveChipsFromPlayersToPot(TableState tableState) {
        final MutableInt moneyToPot = new MutableInt(tableState.getPot());

        tableState.getPlayers().stream().forEach(p -> {
            moneyToPot.add(p.getBetValue());
            p.setBetValue(0);
        });

        tableState.setPot(moneyToPot.intValue());
    }

    public void giveChipsToPlayer(Player player, TableState tableState) {
        player.setBalance(player.getBalance() + tableState.getPot());
    }

    public void splitChipsBetweenPlayers(TableState tableState, Player... players) {
        //TODO code this!
    }
}
