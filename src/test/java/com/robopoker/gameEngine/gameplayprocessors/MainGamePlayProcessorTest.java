package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 18:26
 */
public class MainGamePlayProcessorTest {
    private MainGamePlayProcessor processor;

    private TableState tableStateMock;

    @Before
    public void setUp() throws Exception {
        tableStateMock = mock(TableState.class);
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);

        processor = new MainGamePlayProcessor();
    }

    @Test
    public void shouldAppropriateWhenGameStagePreflop() throws Exception {
        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldNotAppropriateWhenGameStageInit() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.INIT);

        assertFalse(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldAppropriateWhenGameStageFlop() throws Exception {
        when((tableStateMock.getGameStage())).thenReturn(GameStage.FLOP);

        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldAppropriateWhenGameStageTurn() throws Exception {
        when((tableStateMock.getGameStage())).thenReturn(GameStage.TURN);

        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldAppropriateWhenGameStageRiver() throws Exception {
        when((tableStateMock.getGameStage())).thenReturn(GameStage.RIVER);

        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldNotAppropriateWhenGameStageShowdown() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.SHOWDOWN);

        assertFalse(processor.isAppropriate(tableStateMock));
    }
}
