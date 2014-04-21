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
 * Date: 18.04.2014
 * Time: 17:28
 */
public class ShowdownGameProcessorTest {
    public static final PlayerAction FOLD_ACTION = new PlayerAction(PlayerAction.Type.FOLD);
    public static final PlayerAction SIT_OUT_ACTION = new PlayerAction(PlayerAction.Type.SIT_OUT);
    TableState tableStateMock;
    private ShowdownGameProcessor processor;

    private ChipHandler chipHandlerMock;

    private Player firstPlayerMock;
    private Player secondPlayerMock;
    private Player thirdPlayerMock;
    private List<Player> players;

    @Before
    public void setUp() throws Exception {

        tableStateMock = mock(TableState.class);
        chipHandlerMock = mock(ChipHandler.class);

        resetPlayers();
        when(tableStateMock.getGameStage()).thenReturn(GameStage.SHOWDOWN);


        processor = new ShowdownGameProcessor(chipHandlerMock);
    }

    private void resetPlayers() {
        firstPlayerMock = mock(Player.class);
        secondPlayerMock = mock(Player.class);
        thirdPlayerMock = mock(Player.class);

        players = Arrays.asList(firstPlayerMock, secondPlayerMock, thirdPlayerMock);
        players.stream().forEach(p -> when(p.getStatus()).thenReturn(new PlayerAction(PlayerAction.Type.READY)));

        when(tableStateMock.getPlayers()).thenReturn(players);
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

    @Test
    public void shouldGameStageInitWhenProcessorIsInvokes() throws Exception {
        processor.invoke(tableStateMock);

        verify(tableStateMock).setGameStage(GameStage.INIT);
    }

    @Test
    public void shouldChipHandlerRewardFirstPlayerWhenAllOthersAreInFold() throws Exception {
        players.stream().filter(p -> p != firstPlayerMock).forEach(p -> when(p.getStatus()).thenReturn(FOLD_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldChipHandlerRewardSecondPlayerWhenAllOthersAreInFold() throws Exception {
        players.stream().filter(p -> p != secondPlayerMock).forEach(p -> when(p.getStatus()).thenReturn(FOLD_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).giveChipsToPlayer(secondPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNotChipHandlerRewardFirstPlayerWhenAllButFirstAreInFold() throws Exception {
        players.stream().filter(p -> p != secondPlayerMock).forEach(p -> when(p.getStatus()).thenReturn(FOLD_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock, never()).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldChipHandlerRewardFirstPlayerWhenAllOthersAreInSitOut() throws Exception {
        players.stream().filter(p -> p != firstPlayerMock).forEach(p -> when(p.getStatus()).thenReturn(SIT_OUT_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNotChipHandlerRewardFirstPlayerWhenAllButFirstAreInSitOut() throws Exception {
        players.stream().filter(p -> p != secondPlayerMock).forEach(p -> when(p.getStatus()).thenReturn(SIT_OUT_ACTION));

        processor.invoke(tableStateMock);

        verify(chipHandlerMock, never()).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    //TODO when there are more than one active player - need to find player with best cards.
    //TODO need to think about reward transfer if there are more than one winner.


}
