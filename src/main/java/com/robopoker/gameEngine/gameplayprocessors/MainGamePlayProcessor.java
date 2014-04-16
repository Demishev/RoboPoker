package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.PlayerAction;

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

        chipHandler.makeWantedMove(tableState.getPlayers().get(moverNumber), tableState);
        tableState.setLastMovedPlayerNumber(moverNumber);
    }

    private int findMoverNumber(TableState tableState) {
        final int lastMovedPlayerNumber = tableState.getLastMovedPlayerNumber();
        final int playersQuantity = tableState.getPlayers().size();

        int currentPlayerNumber = (lastMovedPlayerNumber == playersQuantity - 1) ? 0 : lastMovedPlayerNumber + 1;

        while (tableState.getPlayers().get(currentPlayerNumber).getStatus().getType() == PlayerAction.Type.FOLD) {
            currentPlayerNumber++;
            if (currentPlayerNumber == playersQuantity + 1) {
                currentPlayerNumber = 0;
            }
        }
        return currentPlayerNumber;
    }
}
