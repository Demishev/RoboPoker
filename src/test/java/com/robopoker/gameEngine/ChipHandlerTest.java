package com.robopoker.gameEngine;

import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * User: Demishev
 * Date: 17.04.2014
 * Time: 12:29
 */
public class ChipHandlerTest {
    public static final int DEFAULT_SMALL_BLIND_VALUE = 100;
    public static final int DEFAULT_BALANCE = 1000;
    public static final PlayerAction FOLD_ACTION = new PlayerAction(PlayerAction.Type.FOLD);
    TableState tableStateMock;
    Player firstPlayerMock;
    Player secondPlayerMock;
    Player thirdPlayerMock;
    List<Player> players;
    private ChipHandler chipHandler;

    @Before
    public void setUp() throws Exception {
        firstPlayerMock = mock(Player.class);
        secondPlayerMock = mock(Player.class);
        thirdPlayerMock = mock(Player.class);

        players = Arrays.asList(firstPlayerMock, secondPlayerMock, thirdPlayerMock);
        players.stream().forEach(p -> when(p.getBalance()).thenReturn(DEFAULT_BALANCE));

        tableStateMock = mock(TableState.class);
        when(tableStateMock.getPlayers()).thenReturn(players);
        when(tableStateMock.getSmallBlindValue()).thenReturn(DEFAULT_SMALL_BLIND_VALUE);

        chipHandler = new ChipHandler();
    }

    @Test
    public void shouldPlayerBetSmallBlindWhenMakeSmallBlind() throws Exception {
        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBetValue(DEFAULT_SMALL_BLIND_VALUE);
    }

    @Test
    public void shouldPlayerBet50WhenMakeSmallBlindTableSmallBlind50() throws Exception {
        when(tableStateMock.getSmallBlindValue()).thenReturn(50);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBetValue(50);
    }

    @Test
    public void shouldPlayerBalanceSet900WhenMakeSmallBlindDefault() throws Exception {
        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBalance(900);
    }

    @Test
    public void shouldPlayerBalanceSet1900WhenMakeSmallBlindDefaultPlayerBalanceIs2000() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(2000);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBalance(1900);
    }

    @Test
    public void shouldPlayerBalanceSet950WhenMakeSmallBlindTableSmallBlind50() throws Exception {
        when(tableStateMock.getSmallBlindValue()).thenReturn(50);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBalance(950);
    }

    @Test
    public void shouldPlayerStatusIsSmallBlind100WhenMakeSmallBlindDefault() throws Exception {
        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.SMALL_BLIND, 100));
    }

    @Test
    public void shouldPlayerStatusIsSmallBlind50WhenMakeSmallBlindDefaultTableSmallBlind50() throws Exception {
        when(tableStateMock.getSmallBlindValue()).thenReturn(50);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.SMALL_BLIND, 50));
    }

    @Test
    public void shouldAllInWhenPlayerBalanceIs10() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(10);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 10));
    }

    @Test
    public void shouldNotPlayerStatusSmallBlind100WhenPlayerBalanceIs10() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(10);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setStatus(new PlayerAction(PlayerAction.Type.SMALL_BLIND, 100));
    }


    @Test
    public void shouldPlayerBalance0WhenPlayerBalanceIs10() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(10);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBalance(0);
    }

    @Test
    public void shouldNotPlayerBalanceMinus90WhenPlayerBalanceIs10() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(10);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setBalance(-90);
    }

    @Test
    public void shouldSetBetValue10WhenPlayerBalanceIs10() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(10);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBetValue(10);
    }

    @Test
    public void shouldSetBetValue20WhenPlayerBalanceIs20() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(20);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBetValue(20);
    }

    @Test
    public void shouldSetStatusAllIn20WhenPlayerBalanceIs20() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(20);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 20));
    }

    @Test
    public void shouldNotSetBetValue100WhenPlayerBalanceIs20() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(20);

        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setBetValue(100);
    }

    @Test
    public void shouldNotSetBetValue1000WhenPlayerBalanceIs1000() throws Exception {
        chipHandler.makeSmallBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setBetValue(1000);
    }

    @Test
    public void shouldSetStatusBigBlind_200ToFirstPlayerWhenMakeBigBlindTransaction() throws Exception {
        chipHandler.makeBigBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BIG_BLIND, 200));
    }

    @Test
    public void shouldSetPlayerBet200WhenBigBlind() throws Exception {
        chipHandler.makeBigBlindTransaction(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setBetValue(200);
    }

    @Test
    public void shouldPlayerBet1000WhenAllInTransaction() throws Exception {
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.ALL_IN));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 1000));
    }

    @Test
    public void shouldPlayerBet500WhenAllInTransactionWhenPlayerBalanceIs500() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(500);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.ALL_IN));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 500));
    }

    @Test
    public void shouldPlayerBet5_000WhenAllInTransactionWhenPlayerBalanceIs5_000() throws Exception {
        when(firstPlayerMock.getBalance()).thenReturn(5_000);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.ALL_IN));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 5_000));
    }

    @Test
    public void shouldNoAllInTransactionWhenBet0() throws Exception {
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setStatus(new PlayerAction(PlayerAction.Type.ALL_IN, 1000));
    }

    @Test
    public void shouldBetValue200WhenPreflopAndNoBetsFromOtherPlayers() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 200));
    }

    @Test
    public void shouldBetValue400WhenPreflopAndNoBetsFromOtherPlayersSmallBlindIs200() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);
        when(tableStateMock.getSmallBlindValue()).thenReturn(200);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 400));
    }

    @Test
    public void shouldBetValue400WhenTurnAndNoBetsFromOtherPlayers() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.TURN);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 400));
    }


    @Test
    public void shouldBetValue200WhenFlopAndNoBetsFromOtherPlayers() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.FLOP);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 200));
    }

    @Test
    public void shouldBetValue500WhenFlopAndNoBetsFromOtherPlayers() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.FLOP);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET, 500));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 500));
    }

    @Test
    public void shouldBetBe500WhenThirdPlayerBetValueIs500() throws Exception {
        when(thirdPlayerMock.getBetValue()).thenReturn(500);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.BET));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.BET, 500));
    }

    @Test
    public void shouldRise200WhenRiseOnPreflop() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.PREFLOP);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.RISE));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.RISE, 200));
    }

    @Test
    public void shouldRise400WhenRiseOnTurn() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.TURN);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.RISE));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.RISE, 400));
    }

    @Test
    public void shouldRise500WhenRise500OnTurn() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.TURN);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.RISE, 500));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.RISE, 500));
    }

    @Test
    public void shouldNoFoldWhenRise() throws Exception {
        when(tableStateMock.getGameStage()).thenReturn(GameStage.TURN);
        when(firstPlayerMock.getWantedMove()).thenReturn(new PlayerAction(PlayerAction.Type.RISE, 500));

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock, never()).setStatus(FOLD_ACTION);
    }

    @Test
    public void shouldFoldWhenFold() throws Exception {
        when(firstPlayerMock.getWantedMove()).thenReturn(FOLD_ACTION);

        chipHandler.makeWantedMove(firstPlayerMock, tableStateMock);

        verify(firstPlayerMock).setStatus(FOLD_ACTION);
    }

    //TODO Check is 0 when was no bets on table or fold otherwise.
}
