package com.robopoker.gameEngine.gameplayprocessors;

import com.robopoker.gameEngine.ChipHandler;
import com.robopoker.gameEngine.TableState;
import com.robopoker.gameStuff.GameStage;
import com.robopoker.gameStuff.Player;
import com.robopoker.gameStuff.PlayerAction;

/**
 * User: Demishev
 * Date: 18.04.2014
 * Time: 17:27
 */
public class ShowdownGameProcessor implements GamePlayProcessor {
    public static final PlayerAction FOLD = new PlayerAction(PlayerAction.Type.FOLD);
    public static final PlayerAction SIT_OUT = new PlayerAction(PlayerAction.Type.SIT_OUT);
    private final ChipHandler chipHandler;

    public ShowdownGameProcessor(ChipHandler chipHandler) {
        this.chipHandler = chipHandler;
    }

    @Override
    public boolean isAppropriate(TableState tableState) {
        return tableState.getGameStage() == GameStage.SHOWDOWN;
    }

    @Override
    public void invoke(TableState tableState) {
        tableState.getPlayers().stream().filter(this::isNotLooserStatus).forEach(p -> chipHandler.giveChipsToPlayer(p, tableState));

        tableState.setGameStage(GameStage.INIT);
    }

    private boolean isNotLooserStatus(Player player) {
        return !(player.getStatus().equals(FOLD) || player.getStatus().equals(SIT_OUT));
    }
}
