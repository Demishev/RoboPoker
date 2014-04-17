package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;
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
 * Time: 18:26
 */
public class MainGamePlayProcessorTest {
    private final PlayerAction READY_PLAYER_ACTION = new PlayerAction(PlayerAction.Type.READY);
    private final PlayerAction CHECK_PLAYER_ACTION = new PlayerAction(PlayerAction.Type.CHECK);
    private final PlayerAction ALL_IN_PLAYER_ACTION = new PlayerAction(PlayerAction.Type.ALL_IN);
    private final PlayerAction FOLD_PLAYER_ACTION = new PlayerAction(PlayerAction.Type.FOLD);
    private final PlayerAction SIT_OUT_PLAYER_ACTION = new PlayerAction(PlayerAction.Type.SIT_OUT);

    private MainGamePlayProcessor processor;

    private TableState tableStateMock;

    private Player firstPlayerMock;
    private Player secondPlayerMock;
    private Player thirdPlayerMock;
    private Player fourthPlayerMock;
    private List<Player> players;

    private ChipHandler chipHandlerMock;

    @Before
    public void setUp() throws Exception {
        resetPlayers();

        tableStateMock = mock(TableState.class);
        when(tableStateMock.getPlayers()).thenReturn(players);
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);
        when(tableStateMock.getLastMovedPlayerNumber()).thenReturn(0);

        chipHandlerMock = mock(ChipHandler.class);

        processor = new MainGamePlayProcessor(chipHandlerMock);
    }

    private void resetPlayers() {
        firstPlayerMock = mock(Player.class);
        secondPlayerMock = mock(Player.class);
        thirdPlayerMock = mock(Player.class);
        fourthPlayerMock = mock(Player.class);

        players = Arrays.asList(firstPlayerMock, secondPlayerMock, thirdPlayerMock, fourthPlayerMock);

        players.stream().forEach(p -> when(p.getStatus()).thenReturn(READY_PLAYER_ACTION));
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

    @Test
    public void shouldSecondMoverMakesCheckWhenDefault() throws Exception {
        when(firstPlayerMock.getWantedMove()).thenReturn(CHECK_PLAYER_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(secondPlayerMock, tableStateMock);
    }

    @Test
    public void shouldThirdMoverMakesCheckWhenDefaultLastMovedPlayerNumberIs1() throws Exception {
        when(thirdPlayerMock.getWantedMove()).thenReturn(CHECK_PLAYER_ACTION);
        when(tableStateMock.getLastMovedPlayerNumber()).thenReturn(1);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(thirdPlayerMock, tableStateMock);
    }

    @Test
    public void shouldFirstMoverMakesCheckWhenDefaultLastMovedPlayerNumberIs3() throws Exception {
        when(firstPlayerMock.getWantedMove()).thenReturn(CHECK_PLAYER_ACTION);
        when(tableStateMock.getLastMovedPlayerNumber()).thenReturn(3);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldLastMoverNumberIs1WhenDefaultInvocation() throws Exception {
        processor.invoke(tableStateMock);

        verify(tableStateMock).setLastMovedPlayerNumber(1);
    }

    @Test
    public void shouldLastMoverNumberIs2WhenDefaultInvocationLastMoverNumberWas1() throws Exception {
        when(tableStateMock.getLastMovedPlayerNumber()).thenReturn(1);
        processor.invoke(tableStateMock);

        verify(tableStateMock).setLastMovedPlayerNumber(2);
    }

    @Test
    public void shouldMoverIsThirdPlayerWhenDefaultInvocationSecondPlayerStatusIsFold() throws Exception {
        when(secondPlayerMock.getStatus()).thenReturn(FOLD_PLAYER_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(thirdPlayerMock, tableStateMock);
    }

    @Test
    public void shouldMoverIsForthPlayerWhenDefaultInvocationSecondAndThirdPlayersStatusesAreFold() throws Exception {
        when(secondPlayerMock.getStatus()).thenReturn(FOLD_PLAYER_ACTION);
        when(thirdPlayerMock.getStatus()).thenReturn(FOLD_PLAYER_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(fourthPlayerMock, tableStateMock);
    }

    @Test
    public void shouldSetNullToWantedMoveToSecondPlayerWhenDefaultInvocation() throws Exception {
        processor.invoke(tableStateMock);

        verify(secondPlayerMock).setWantedMove(null);
    }

    @Test
    public void shouldMoverIsThirdPlayerWhenDefaultInvocationSecondPlayerStatusIsAllIn() throws Exception {
        when(secondPlayerMock.getStatus()).thenReturn(ALL_IN_PLAYER_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(thirdPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNoMoveFirstPlayerWhenAllPlayerStatusesButFirstAreFold() throws Exception {
        players.stream().filter(p -> p != firstPlayerMock)
                .forEach(p -> when(p.getStatus()).thenReturn(FOLD_PLAYER_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock, never()).makeWantedMove(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldThirdPlayerMoveWhenDefaultAndSecondPlayerStatusIsSitOut() throws Exception {
        when(secondPlayerMock.getStatus()).thenReturn(SIT_OUT_PLAYER_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).makeWantedMove(thirdPlayerMock, tableStateMock);
    }

    //TODO when all are in call and one in rise. All with equal money on pot. -> go to next round.

    //TODO preflop - flop.
    //TODO flop - turn.
    //TODO turn - river.
    //TODO river - showdown.

    //TODO When go to next round - need to update statuses.
    //TODO When go to next round - need to add money to pot from players. (ChipHandler?)
    //TODO When player has skipped status it needs to remain.
    //TODO When status is not skipable - in needs to be changed on READY.
    //TODO when all are in fold and one is not. -> go to final round.

}
