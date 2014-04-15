package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.messaging.MessageEngine;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 18:25
 */
public class MainGamePlayProcessor implements GamePlayProcessor {
    @Override
    public boolean isAppropriate(TableState tableState) {
        final GameStage gameStage = tableState.getGameStage();
        return gameStage == GameStage.PREFLOP || gameStage == GameStage.FLOP || gameStage == GameStage.TURN || gameStage == GameStage.RIVER;
    }

    @Override
    public void invoke(TableState tableState, MessageEngine messageEngine) {

    }
}
