package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.*;
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
    private ChipHandler chipHandlerMock;

    private CardDeckFactory cardDeckFactoryMock;
    private CardDeck cardDeckMock;

    private Card firstCardMock;
    private Card secondCardMock;
    private Card thirdCardMock;
    private Card fourthCardMock;

    @Before
    public void setUp() throws Exception {
        chipHandlerMock = mock(ChipHandler.class);
        resetCardDeckFactory();

        processor = new InitGamePlayProcessor(chipHandlerMock, cardDeckFactoryMock);

        tableStateMock = mock(TableState.class);
        when(tableStateMock.getGameStage()).thenReturn(GameStage.INIT);
        when(tableStateMock.getDealerNumber()).thenReturn(-1);

        firstPlayerMock = mock(Player.class);
        secondPlayerMock = mock(Player.class);

        List<Player> players = Arrays.asList(firstPlayerMock, secondPlayerMock);

        when(tableStateMock.getPlayers()).thenReturn(players);

        messageEngineMock = mock(MessageEngine.class);
    }

    private void resetCardDeckFactory() {
        cardDeckFactoryMock = mock(CardDeckFactory.class);
        cardDeckMock = mock(CardDeck.class);
        firstCardMock = mock(Card.class);
        secondCardMock = mock(Card.class);
        thirdCardMock = mock(Card.class);
        fourthCardMock = mock(Card.class);

        when(cardDeckFactoryMock.generateNewCardDeck()).thenReturn(cardDeckMock);
        when(cardDeckMock.getCard())
                .thenReturn(firstCardMock)
                .thenReturn(secondCardMock)
                .thenReturn(thirdCardMock)
                .thenReturn(fourthCardMock);
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

    @Test
    public void shouldAllPlayersSetStatusReadyWhenInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(firstPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.READY, 0));
        verify(secondPlayerMock).setStatus(new PlayerAction(PlayerAction.Type.READY, 0));
    }

    @Test
    public void shouldSecondPlayerGoesSmallBlindWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(chipHandlerMock).makeSmallBlindTransaction(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNotFirstPlayerGoesSmallBlindWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(chipHandlerMock, never()).makeSmallBlindTransaction(secondPlayerMock, tableStateMock);
    }

    @Test
    public void shouldFirstPlayerGoesBigBlindWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(chipHandlerMock).makeBigBlindTransaction(secondPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNotSecondPlayerGoesBigBlindWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(chipHandlerMock, never()).makeBigBlindTransaction(firstPlayerMock, tableStateMock);
    }

    @Test
    public void shouldSetLastMoverNumberBigBlindMoverWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setLastMovedPlayerNumber(1);
    }

    @Test
    public void shouldSetLastMoverNumberBigBlindMoverWhenDealerWas0() throws Exception {
        when(tableStateMock.getDealerNumber()).thenReturn(0);

        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setLastMovedPlayerNumber(0);
    }

    @Test
    public void shouldSetGameStagePreflopWhenInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setGameStage(GameStage.PREFLOP);
    }

    @Test
    public void shouldSetCardDeckToTableStateWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(tableStateMock).setCardDeck(cardDeckMock);
    }

    @Test
    public void shouldGiveCardsToPlayersWhenDefaultInvoke() throws Exception {
        processor.invoke(tableStateMock, messageEngineMock);

        verify(firstPlayerMock).setPlayerCards(Arrays.asList(firstCardMock, secondCardMock));
        verify(secondPlayerMock).setPlayerCards(Arrays.asList(thirdCardMock, fourthCardMock));
    }
}
