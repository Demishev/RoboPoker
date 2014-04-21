package com.robopoker.gameStuff;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 12:36
 */
public enum GameStage {
    INIT, PREFLOP, FLOP, TURN, RIVER, SHOWDOWN;

    public GameStage nextGameStage() {
        final int ordinal = this.ordinal();

        return GameStage.values()[ordinal + 1];
    }
}
