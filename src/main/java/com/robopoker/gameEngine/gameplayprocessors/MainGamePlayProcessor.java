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
        final Player mover = tableState.getPlayers().get(moverNumber);

        chipHandler.makeWantedMove(mover, tableState);
        mover.setWantedMove(null);
        tableState.setLastMovedPlayerNumber(moverNumber);
    }

    private int findMoverNumber(TableState tableState) {
        final int lastMovedPlayerNumber = tableState.getLastMovedPlayerNumber();
        final int playersQuantity = tableState.getPlayers().size();

        int currentPlayerNumber = (lastMovedPlayerNumber == playersQuantity - 1) ? 0 : lastMovedPlayerNumber + 1;

        while (isSkippedPlayerStatus(tableState, currentPlayerNumber)) {
            currentPlayerNumber++;
            if (currentPlayerNumber == playersQuantity + 1) {
                currentPlayerNumber = 0;
            }
        }
        return currentPlayerNumber;
    }

    private boolean isSkippedPlayerStatus(TableState tableState, int currentPlayerNumber) {
        List<PlayerAction.Type> skippedStatuses = Arrays.asList(PlayerAction.Type.FOLD, PlayerAction.Type.ALL_IN);
        PlayerAction.Type playerStatus = tableState.getPlayers().get(currentPlayerNumber).getStatus().getType();

        return skippedStatuses.stream().filter(status -> status == playerStatus).count() != 0;
    }
}
