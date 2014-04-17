package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

import java.util.Arrays;
import java.util.List;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 18:25
 */
public class MainGamePlayProcessor implements GamePlayProcessor {
    private final ChipHandler chipHandler;

    public MainGamePlayProcessor(ChipHandler chipHandler) {
        this.chipHandler = chipHandler;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        final GameStage gameStage = tableState.getGameStage();
        return gameStage == GameStage.PREFLOP || gameStage == GameStage.FLOP || gameStage == GameStage.TURN || gameStage == GameStage.RIVER;
    }

    @Override
    public void invoke(TableState tableState) {
        final int moverNumber = findMoverNumber(tableState);

        if (moverNumber != -1) {
            final Player mover = tableState.getPlayers().get(moverNumber);

            chipHandler.makeWantedMove(mover, tableState);
            mover.setWantedMove(null);
            tableState.setLastMovedPlayerNumber(moverNumber);
        }
    }

    private int findMoverNumber(TableState tableState) {
        final List<Player> players = tableState.getPlayers();

        if (players.stream().filter(p -> !isSkippedPlayerStatus(p)).count() < 2) {
            return -1;
        }

        final int lastMovedPlayerNumber = tableState.getLastMovedPlayerNumber();
        final int playersQuantity = players.size();

        int currentPlayerNumber = (lastMovedPlayerNumber == playersQuantity - 1) ? 0 : lastMovedPlayerNumber + 1;

        while (lastMovedPlayerNumber != currentPlayerNumber &&
                (isSkippedPlayerStatus(tableState.getPlayers().get(currentPlayerNumber)) ||
                        !playerNeedsToMakeABet(players, currentPlayerNumber))) {
            currentPlayerNumber++;
            if (currentPlayerNumber == playersQuantity) {
                currentPlayerNumber = 0;
            }
        }
        return lastMovedPlayerNumber != currentPlayerNumber ? currentPlayerNumber : -1;
    }

    private boolean playerNeedsToMakeABet(List<Player> players, int currentPlayerNumber) {
        final int maxBet = findMaxBet(players);
        return players.get(currentPlayerNumber).getBetValue() < maxBet ||
                players.get(currentPlayerNumber).getStatus().getType() == PlayerAction.Type.READY;
    }

    private int findMaxBet(List<Player> players) {
        return players.stream().max((o1, o2) -> o1.getBetValue() - o2.getBetValue()).get().getBetValue();
    }

    private boolean isSkippedPlayerStatus(Player player) {
        List<PlayerAction.Type> skippedStatuses =
                Arrays.asList(PlayerAction.Type.FOLD, PlayerAction.Type.ALL_IN, PlayerAction.Type.SIT_OUT);

        PlayerAction.Type playerStatus = player.getStatus().getType();

        return skippedStatuses.stream().filter(status -> status == playerStatus).count() != 0;
    }
}
