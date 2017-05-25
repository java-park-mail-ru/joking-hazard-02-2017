package sample.Game.Mechanics.States;

import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.ServerFinishedMessage;
import sample.Game.Mechanics.GameContext;

import java.util.Map;

/**
 * Created by ksg on 27.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class FinishState extends GameState {
    public FinishState(GameContext context) {
        this.context = context;
    }

    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        return ErrorCodes.INVALID_COMMAND;
    }

    @Override
    public ErrorCodes transfer() {
        context.state = this;
        final ServerFinishedMessage msg = new ServerFinishedMessage(context.mapper);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            entry.getValue().sendMessage(msg);
            entry.getValue().resetMessage();
        }
        return ErrorCodes.FINISHED;
    }
}
