package com.robopoker.gameStuff;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Demishev
 * Date: 20.04.2014
 * Time: 13:08
 */
public class GameStageTest {

    @Test
    public void shouldPreflopGameStageWhenInitGameStageNext() throws Exception {
        assertEquals(GameStage.PREFLOP, GameStage.INIT.nextGameStage());
    }
}
