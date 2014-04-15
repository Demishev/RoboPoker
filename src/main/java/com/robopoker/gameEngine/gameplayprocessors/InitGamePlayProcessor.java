package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.messaging.MessageEngine;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 12:54
 */
public class InitGamePlayProcessor implements GamePlayProcessor {

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState != null && tableState.getGameStage() == GameStage.INIT;
    }

    @Override
    public void invoke(TableState tableState, MessageEngine messageEngine) {
        changeDealerNumber(tableState);
    }

    private void changeDealerNumber(TableState tableState) {
        int dealerNumber = tableState.getDealerNumber() + 1;

        tableState.setDealerNumber(dealerNumber >= tableState.getPlayers().size() ? 0 : dealerNumber);
    }
}
