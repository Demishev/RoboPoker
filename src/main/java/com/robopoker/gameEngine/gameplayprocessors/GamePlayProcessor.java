package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;

/**
 * User: Demishev
 * Date: 14.04.2014
 * Time: 18:09
 */
public interface GamePlayProcessor {
    boolean isAppropriate(TableState tableState);

    void invoke(TableState tableState);
}
