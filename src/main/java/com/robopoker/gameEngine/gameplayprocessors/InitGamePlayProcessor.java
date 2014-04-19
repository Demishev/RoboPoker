package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.*;

import java.util.Arrays;
import java.util.List;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 12:54
 */
public class InitGamePlayProcessor implements GamePlayProcessor {
    private final ChipHandler chipHandler;
    private final CardDeckFactory cardDeckFactory;

    public InitGamePlayProcessor(ChipHandler chipHandler, CardDeckFactory cardDeckFactory) {
        this.chipHandler = chipHandler;
        this.cardDeckFactory = cardDeckFactory;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState != null && tableState.getGameStage() == GameStage.INIT;
    }

    @Override
    public void invoke(TableState tableState) {
        changeDealerNumber(tableState);
        resetPlayersStatuses(tableState.getPlayers());
        makeSmallAndBigBlindBets(tableState);

        setCards(tableState);

        tableState.setGameStage(GameStage.PREFLOP);
    }

    private void setCards(TableState tableState) {
        final CardDeck cardDeck = cardDeckFactory.generateNewCardDeck();

        tableState.setCardDeck(cardDeck);

        tableState.getPlayers().stream().forEach(p -> p.setPlayerCards(Arrays.asList(cardDeck.getCard(), cardDeck.getCard())));

        List<Card> descCards = Arrays.asList(cardDeck.getCard(), cardDeck.getCard(), cardDeck.getCard(), cardDeck.getCard(), cardDeck.getCard());

        tableState.setDeskCards(descCards);
    }

    private void makeSmallAndBigBlindBets(TableState tableState) {
        int smallBlindPlayerNumber = findNextPlayerNumber(tableState.getDealerNumber(), tableState);
        chipHandler.makeSmallBlindTransaction(tableState.getPlayers().get(smallBlindPlayerNumber), tableState);

        int bigBlindPlayerNumber = findNextPlayerNumber(smallBlindPlayerNumber, tableState);
        chipHandler.makeBigBlindTransaction(tableState.getPlayers().get(bigBlindPlayerNumber), tableState);

        tableState.setLastMovedPlayerNumber(bigBlindPlayerNumber);
    }

    private void resetPlayersStatuses(List<Player> players) {
        players.stream().forEach(player -> player.setStatus(new PlayerAction(PlayerAction.Type.READY, 0)));
    }

    private void changeDealerNumber(TableState tableState) {
        tableState.setDealerNumber(findNextPlayerNumber(tableState.getDealerNumber(), tableState));
    }

    private int findNextPlayerNumber(int previousPlayerNumber, TableState tableState) {
        return (previousPlayerNumber + 1 >= tableState.getPlayers().size()) ? 0 : previousPlayerNumber + 1;
    }
}
