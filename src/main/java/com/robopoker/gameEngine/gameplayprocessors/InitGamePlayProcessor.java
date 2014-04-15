package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;
import com.robopoker.messaging.MessageEngine;

import java.util.List;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 12:54
 */
public class InitGamePlayProcessor implements GamePlayProcessor {
    private final ChipHandler chipHandler;

    public InitGamePlayProcessor(ChipHandler chipHandler) {
        this.chipHandler = chipHandler;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState != null && tableState.getGameStage() == GameStage.INIT;
    }

    @Override
    public void invoke(TableState tableState, MessageEngine messageEngine) {
        changeDealerNumber(tableState);
        resetPlayersStatuses(tableState.getPlayers());
        makeSmallAndBigBlindBets(tableState);

        tableState.setGameStage(GameStage.PREFLOP);
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
