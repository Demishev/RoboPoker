package com.robopoker.gameEngine;

import com.robopoker.gameEngine.gamecommand.GameCommand;
import com.robopoker.gameEngine.gamecommandsprocessor.GameCommandProcessor;
import com.robopoker.gameEngine.gameplayprocessors.GamePlayProcessor;
import com.robopoker.messaging.MessageEngine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: Demishev
 * Date: 14.04.2014
 * Time: 14:23
 */
public class TableEngineTest {
    private TableState tableStateMock;

    private MessageEngine messageEngineMock;

    private GameCommand gameCommandMock;
    private GameCommandProcessor firstGameCommandProcessorMock;
    private GameCommandProcessor secondGameCommandProcessorMock;

    private GamePlayProcessor firstGamePlayProcessorMock;
    private GamePlayProcessor secondGamePlayProcessorMock;


    private TableEngine tableEngine;

    @Before
    public void setUp() throws Exception {
        tableStateMock = mock(TableState.class);
        when(tableStateMock.getCommands()).thenReturn(new ArrayList<>());

        gameCommandMock = mock(GameCommand.class);

        firstGameCommandProcessorMock = mock(GameCommandProcessor.class);
        secondGameCommandProcessorMock = mock(GameCommandProcessor.class);
        List<GameCommandProcessor> gameCommandProcessorList = Arrays.asList
                (firstGameCommandProcessorMock, secondGameCommandProcessorMock);

        firstGamePlayProcessorMock = mock(GamePlayProcessor.class);
        when(firstGamePlayProcessorMock.isAppropriate(tableStateMock)).thenReturn(true);
        secondGamePlayProcessorMock = mock(GamePlayProcessor.class);
        List<GamePlayProcessor> gamePlayProcessorList = Arrays.asList
                (firstGamePlayProcessorMock, secondGamePlayProcessorMock);

        messageEngineMock = mock(MessageEngine.class);

        tableEngine = new TableEngine(tableStateMock, gameCommandProcessorList, gamePlayProcessorList, messageEngineMock);
    }

    @Test
    public void shouldDoNothingWithCommandsWhenTableEngineUpdateNoCommands() throws Exception {
        tableEngine.update();
    }

    @Test
    public void shouldVerifyPlayerCommandsWhenUpdate() throws Exception {
        when(tableStateMock.getCommands()).thenReturn(new ArrayList<>(Arrays.asList(gameCommandMock)));

        tableEngine.update();

        verify(firstGameCommandProcessorMock).isAppropriate(gameCommandMock);
        verify(secondGameCommandProcessorMock).isAppropriate(gameCommandMock);
    }

    @Test
    public void should0CommandsWhenUpdateDone() throws Exception {
        final ArrayList<GameCommand> gameCommands = new ArrayList<>(Arrays.asList(gameCommandMock));
        when(tableStateMock.getCommands()).thenReturn(gameCommands);

        tableEngine.update();

        assertEquals(0, gameCommands.size());
    }

    @Test
    public void shouldInvokeFirstCommandProcessorAndIgnoreSecondWhenFirstIsAppropriate() throws Exception {
        when(tableStateMock.getCommands()).thenReturn(new ArrayList<>(Arrays.asList(gameCommandMock)));
        when(firstGameCommandProcessorMock.isAppropriate(gameCommandMock)).thenReturn(true);

        tableEngine.update();

        verify(firstGameCommandProcessorMock).invoke(tableStateMock, gameCommandMock, messageEngineMock);
        verify(secondGameCommandProcessorMock, never()).invoke(tableStateMock, gameCommandMock, messageEngineMock);
    }

    @Test
    public void shouldNotInvokeAnyCommandProcessorAndIgnoreSecondWhenThereAreNotAppropriate() throws Exception {
        when(tableStateMock.getCommands()).thenReturn(new ArrayList<>(Arrays.asList(gameCommandMock)));

        tableEngine.update();

        verify(firstGameCommandProcessorMock, never()).invoke(tableStateMock, gameCommandMock, messageEngineMock);
        verify(secondGameCommandProcessorMock, never()).invoke(tableStateMock, gameCommandMock, messageEngineMock);
    }

    @Test
    public void shouldInvokeFirstGameProcessorWhenUpdate() throws Exception {
        tableEngine.update();

        verify(firstGamePlayProcessorMock).invoke(tableStateMock, messageEngineMock);
        verify(secondGamePlayProcessorMock, never()).invoke(tableStateMock, messageEngineMock);
    }

    @Test
    public void shouldSecondProcessorNotInvokedWhenBothAreAppropriate() throws Exception {
        when(secondGamePlayProcessorMock.isAppropriate(tableStateMock)).thenReturn(true);

        tableEngine.update();

        verify(firstGamePlayProcessorMock).invoke(tableStateMock, messageEngineMock);
        verify(secondGamePlayProcessorMock, never()).invoke(tableStateMock, messageEngineMock);
    }
}
