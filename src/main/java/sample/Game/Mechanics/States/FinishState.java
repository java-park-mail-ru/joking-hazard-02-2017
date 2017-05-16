package sample.Game.Mechanics.States;

import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.ServerMessages.ServerFinishedMessage;
import sample.Game.Messages.SystemMessages.MessageContainer;

import java.util.Map;

/**
 * Created by ksg on 27.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class FinishState extends GameState {
    public FinishState(MainMechanics.GameContext context){
        this.context = context;
    }
    public ErrorCodes handle(MessageContainer msg){
        return ErrorCodes.INVALID_COMMAND;
    }
    public ErrorCodes transfer(){
        context.state = this;
        ServerFinishedMessage msg = new ServerFinishedMessage(context.mapper);
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            entry.getValue().sendMessage(msg);
        }
        return ErrorCodes.OK;
    }
}