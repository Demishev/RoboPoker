package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.messaging.MessageEngine;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 12:55
 */
public class InitGamePlayProcessorTest {
    private final Player thirdPlayerMock = mock(Player.class);
    private InitGamePlayProcessor processor;

    private TableState tableStateMock;
    private Player firstPlayerMock;
    private Player secondPlayerMock;
    private MessageEngine messageEngineMock;

    @Before
    public void setUp() throws Exception {
        processor = new InitGamePlayProcessor();

        tableStateMock = mock(TableState.class);
        when(tableStateMock.getGameStage()).thenReturn(GameStage.INIT);
        when(tableStateMock.getDealerNumber()).thenReturn(-1);

        firstPlayerMock = mock(Player.class);
        secondPlayerMock = mock(Player.class);

        List<Player> players = Arrays.asList(firstPlayerMock, secondPlayerMock);

        when(tableStateMock.getPlayers()).thenReturn(players);

        messageEngineMock = mock(MessageEngine.class);
    }

    @Test
    public void shouldIsNotAppropriateWithNull() throws Exception {
        assertFalse(processor.isAppropriate(null));
    }

    @Test
    public void shouldTrueWhenTableStateHasInitGameState() throws Exception {
        assertTrue(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldFalseWhenGameStateIsPreflop() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);

        assertFalse(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldFalseWhenGameStateIsNull() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(null);

        assertFalse(processor.isAppropriate(tableStateMock));
    }

    @Test
    public void shouldSetDealerNumber0WhenDealerNumberWasMinusOne() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setDealerNumber(0);
    }

    @Test
    public void shouldSetDealerNumber1WhenDealerNumber0() throws Exception {
        when(tableStateMock.getDealerNumber()).thenReturn(0);

        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setDealerNumber(1);
    }

    @Test
    public void shouldSetDealerNumber0WhenDealerNumber1() throws Exception {
        when(tableStateMock.getDealerNumber()).thenReturn(1);

        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setDealerNumber(0);
    }

    @Test
    public void shouldSetDealerNumber2WhenDealerNumber1AndThereAre3Players() throws Exception {
        when(tableStateMock.getDealerNumber()).thenReturn(1);
        when(tableStateMock.getPlayers())
                .thenReturn(Arrays.asList(firstPlayerMock, secondPlayerMock, thirdPlayerMock));

        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setDealerNumber(2);
    }
}
