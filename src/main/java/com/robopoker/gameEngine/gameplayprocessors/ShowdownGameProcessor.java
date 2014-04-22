package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.CardCombinationFactory;
import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.Card;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

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
    private final CardCombinationFactory cardCombinationFactory;

    public ShowdownGameProcessor(ChipHandler chipHandler, CardCombinationFactory cardCombinationFactory) {
        this.chipHandler = chipHandler;
        this.cardCombinationFactory = cardCombinationFactory;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState.getGameStage() == GameStage.SHOWDOWN;
    }

    @Override
    public void invoke(TableState tableState) {
        final List<Card> descCards = tableState.getDeskCards();

        chipHandler.giveChipsToPlayer(tableState.getPlayers().stream().filter(this::isNotLooserStatus)
                .max((o1, o2) -> cardCombinationFactory.generateCardCombination(o1.getPlayerCards(), descCards)
                        .compareTo(cardCombinationFactory.generateCardCombination(o2.getPlayerCards(), descCards)))
                .get(), tableState);

        tableState.setGameStage(GameStage.INIT);
    }

    private boolean isNotLooserStatus(Player player) {
        return !(player.getStatus().equals(FOLD) || player.getStatus().equals(SIT_OUT));
    }
}
