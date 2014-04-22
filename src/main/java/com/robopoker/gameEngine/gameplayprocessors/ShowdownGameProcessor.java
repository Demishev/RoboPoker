package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.CardCombinationFactory;
import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.Card;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Demishev
 * Date: 18.04.2014
 * Time: 17:27
 */
public class ShowdownGameProcessor implements GamePlayProcessor {
    public static final PlayerAction FOLD = new PlayerAction(PlayerAction.Type.FOLD);
    public static final PlayerAction SIT_OUT = new PlayerAction(PlayerAction.Type.SIT_OUT);
    private final ChipHandler chipHandler;
    private final CardCombinationFactory combinationFactory;

    public ShowdownGameProcessor(ChipHandler chipHandler, CardCombinationFactory combinationFactory) {
        this.chipHandler = chipHandler;
        this.combinationFactory = combinationFactory;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState.getGameStage() == GameStage.SHOWDOWN;
    }

    @Override
    public void invoke(TableState tableState) {
        final List<Card> descCards = tableState.getDeskCards();

        final List<Player> activePlayers = new ArrayList<>();

        tableState.getPlayers().stream().filter(this::isNotLooserStatus)
                .forEach(activePlayers::add);

        final int maxScore = combinationFactory.generateCardCombination(activePlayers.stream().max((o1, o2) -> {
            return combinationFactory.generateCardCombination(o1.getPlayerCards(), descCards).combinationValue() -
                    combinationFactory.generateCardCombination(o2.getPlayerCards(), descCards).combinationValue();
        }).get().getPlayerCards(), descCards).combinationValue();

        final List<Player> winners = new ArrayList<>();

        activePlayers.stream().forEach(p -> {
            int playerScore = combinationFactory.generateCardCombination(p.getPlayerCards(), descCards).combinationValue();
            if (playerScore == maxScore) {
                winners.add(p);
            }
        });

        if (winners.size() == 1) {
            chipHandler.giveChipsToPlayer(winners.get(0), tableState);
        } else {
            chipHandler.splitChipsBetweenPlayers(tableState, winners.toArray(new Player[winners.size()]));
        }

        tableState.setGameStage(GameStage.INIT);
    }

    private boolean isNotLooserStatus(Player player) {
        return !(player.getStatus().equals(FOLD) || player.getStatus().equals(SIT_OUT));
    }
}
