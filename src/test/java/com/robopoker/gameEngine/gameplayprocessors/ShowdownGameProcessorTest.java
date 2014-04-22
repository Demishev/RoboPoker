package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.CardCombinationFactory;
import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.*;
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
    private final Card firstPlayerCard1Mock = mock(Card.class);
    private final Card firstPlayerCard2Mock = mock(Card.class);
    private final Card secondPlayerCard1Mock = mock(Card.class);
    private final Card secondPlayerCard2Mock = mock(Card.class);
    private final Card thirdPlayerCard1Mock = mock(Card.class);
    private final Card thirdPlayerCard2Mock = mock(Card.class);
    private final Card firstDeskCardMock = mock(Card.class);
    private final Card secondDeskCardMock = mock(Card.class);
    TableState tableStateMock;
    CardCombination firstPlayerCardCombinationMock;
    CardCombination secondPlayerCardCombinationMock;
    CardCombination thirdPlayerCardCombinationMock;
    private ShowdownGameProcessor processor;
    private ChipHandler chipHandlerMock;
    private CardCombinationFactory cardCombinationFactoryMock;
    private Player firstPlayerMock;
    private Player secondPlayerMock;
    private Player thirdPlayerMock;
    private List<Player> players;

    @Before
    public void setUp() throws Exception {
        tableStateMock = mock(TableState.class);
        chipHandlerMock = mock(ChipHandler.class);
        cardCombinationFactoryMock = mock(CardCombinationFactory.class);

        resetPlayers();

        when(tableStateMock.getGameStage()).thenReturn(GameStage.SHOWDOWN);

        resetCardCombinations();

        processor = new ShowdownGameProcessor(chipHandlerMock, cardCombinationFactoryMock);
    }

    private void resetCardCombinations() {
        firstPlayerCardCombinationMock = mock(CardCombination.class);
        secondPlayerCardCombinationMock = mock(CardCombination.class);
        thirdPlayerCardCombinationMock = mock(CardCombination.class);

        List<Card> firstPlayerCards = Arrays.asList(firstPlayerCard1Mock, firstPlayerCard2Mock);
        List<Card> secondPlayerCards = Arrays.asList(secondPlayerCard1Mock, secondPlayerCard2Mock);
        List<Card> thirdPlayerCards = Arrays.asList(thirdPlayerCard1Mock, thirdPlayerCard2Mock);

        List<Card> descCards = Arrays.asList(firstDeskCardMock, secondDeskCardMock);

        when(firstPlayerMock.getPlayerCards()).thenReturn(firstPlayerCards);
        when(secondPlayerMock.getPlayerCards()).thenReturn(secondPlayerCards);
        when(thirdPlayerMock.getPlayerCards()).thenReturn(thirdPlayerCards);

        when(tableStateMock.getDeskCards()).thenReturn(descCards);

        when(cardCombinationFactoryMock.generateCardCombination(firstPlayerCards, descCards)).thenReturn(firstPlayerCardCombinationMock);
        when(cardCombinationFactoryMock.generateCardCombination(secondPlayerCards, descCards)).thenReturn(secondPlayerCardCombinationMock);
        when(cardCombinationFactoryMock.generateCardCombination(thirdPlayerCards, descCards)).thenReturn(thirdPlayerCardCombinationMock);
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

    @Test
    public void shouldFirstPlayerBeRewardedWhenAllAreReadyAndFirstHasBestCardCombination() throws Exception {
        setFirstSecondThirdCardCombinationIsBest();

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    private void setFirstSecondThirdCardCombinationIsBest() {
        when(firstPlayerCardCombinationMock.compareTo(secondPlayerCardCombinationMock)).thenReturn(5);
        when(secondPlayerCardCombinationMock.compareTo(firstPlayerCardCombinationMock)).thenReturn(-5);

        when(firstPlayerCardCombinationMock.compareTo(thirdPlayerCardCombinationMock)).thenReturn(3);
        when(thirdPlayerCardCombinationMock.compareTo(firstPlayerCardCombinationMock)).thenReturn(-3);

        when(secondPlayerCardCombinationMock.compareTo(thirdPlayerCardCombinationMock)).thenReturn(1);
        when(thirdPlayerCardCombinationMock.compareTo(secondPlayerCardCombinationMock)).thenReturn(-1);
    }


    @Test
    public void shouldThirdPlayerNotBeRewardedWhenAllAreReadyAndFirstHasBestCardCombination() throws Exception {
        setFirstSecondThirdCardCombinationIsBest();

        processor.invoke(tableStateMock);

        verify(chipHandlerMock, never()).giveChipsToPlayer(thirdPlayerMock, tableStateMock);
    }

    @Test
    public void shouldSecondPlayerGetsChipsWhenFirstSecondThirdByCoolestAndFirstIsDown() throws Exception {
        setFirstSecondThirdCardCombinationIsBest();
        when(firstPlayerMock.getStatus()).thenReturn(FOLD_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock).giveChipsToPlayer(secondPlayerMock, tableStateMock);
    }

    @Test
    public void shouldNoFirstPlayerGetsChipsWhenFirstSecondThirdByCoolestAndFirstIsDown() throws Exception {
        setFirstSecondThirdCardCombinationIsBest();
        when(firstPlayerMock.getStatus()).thenReturn(FOLD_ACTION);

        processor.invoke(tableStateMock);

        verify(chipHandlerMock, never()).giveChipsToPlayer(firstPlayerMock, tableStateMock);
    }

    //TODO need to think about reward transfer if there are more than one winner.


}
