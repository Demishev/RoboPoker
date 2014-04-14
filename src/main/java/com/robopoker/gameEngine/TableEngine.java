package com.robopoker.gameEngine;

import com.robopoker.gameEngine.gamecommand.GameCommand;
import com.robopoker.gameEngine.gamecommandsprocessor.GameCommandProcessor;
import com.robopoker.gameEngine.gameplayprocessors.GamePlayProcessor;
import com.robopoker.messaging.MessageEngine;

import java.util.List;

/**
 * User: Demishev
 * Date: 14.04.2014
 * Time: 14:23
 */
public class TableEngine {
    private final TableState tableState;
    private final List<GameCommandProcessor> gameCommandProcessorList;
    private final List<GamePlayProcessor> gamePlayProcessorList;
    private final MessageEngine messageEngine;

    public TableEngine(TableState tableState, List<GameCommandProcessor> gameCommandProcessorList, List<GamePlayProcessor> gamePlayProcessorList, MessageEngine messageEngine) {
        this.tableState = tableState;
        this.gameCommandProcessorList = gameCommandProcessorList;
        this.gamePlayProcessorList = gamePlayProcessorList;
        this.messageEngine = messageEngine;
    }

    public void update() {
        performGameCommands();
        makeMove();
    }

    private void performGameCommands() {
        final List<GameCommand> gameCommands = tableState.getCommands();
        while (gameCommands.size() > 0) {
            GameCommand gameCommand = gameCommands.get(0);
            gameCommandProcessorList.stream()
                    .filter(processor -> processor.isAppropriate(gameCommand))
                    .forEach(processor -> processor.invoke(tableState, gameCommand, messageEngine));
            gameCommands.remove(gameCommand);
        }
    }

    private void makeMove() {
        gamePlayProcessorList.stream()
                .filter(processor -> processor.isAppropriate(tableState))
                .findFirst().get().invoke(tableState, messageEngine);
    }
}
