package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;

/**
 * User: Demishev
 * Date: 18.04.2014
 * Time: 17:27
 */
public class ShowdownGameProcessor implements GamePlayProcessor {

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState.getGameStage() == GameStage.SHOWDOWN;
    }

    @Override
    public void invoke(TableState tableState) {

    }
}
