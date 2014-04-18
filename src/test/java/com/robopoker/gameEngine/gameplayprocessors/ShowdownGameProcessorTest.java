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
 * Date: 18.04.2014
 * Time: 17:28
 */
public class ShowdownGameProcessorTest {
    TableState tableStateMock;
    private ShowdownGameProcessor processor;

    @Before
    public void setUp() throws Exception {
        processor = new ShowdownGameProcessor();

        tableStateMock = mock(TableState.class);
        when(tableStateMock.getGameStage()).thenReturn(GameStage.SHOWDOWN);
    }

    @Test
    public void shouldAppropriateWhenGameStageShowdown() throws Exception {
        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldNotAppropriateWhenPreflopGameStage() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);

        assertFalse(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldNotAppropriateWhenGameStageIsNull() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(null);

        assertFalse(processor.isAppropriate(tableStateMock));
    }
}
