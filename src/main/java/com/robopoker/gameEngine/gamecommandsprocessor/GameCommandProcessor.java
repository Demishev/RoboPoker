package com.robopoker.gameEngine.gamecommandsprocessor;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameEngine.gamecommand.GameCommand;

/**
 * User: Demishev
 * Date: 14.04.2014
 * Time: 18:09
 */
public interface GameCommandProcessor {
    boolean isAppropriate(GameCommand gameCommand);

    void invoke(TableState tableState, GameCommand gameCommand);
}
