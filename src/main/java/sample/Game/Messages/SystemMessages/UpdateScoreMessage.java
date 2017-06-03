package sample.Game.Messages.SystemMessages;

import sample.Game.Messages.ServerMessages.RoundInfo;

/**
 * Created by ksg on 01.06.17.
 */
public class UpdateScoreMessage extends BaseSystemMessage {
    RoundInfo info;
    public UpdateScoreMessage(RoundInfo info){
        this.info = info;
    }

    @Override
    public String getType(){
        return "UpdateScoreMessage";
    }

    @Override
    public Class getClassOfMessage() {
        return UpdateScoreMessage.class;
    }

    public RoundInfo getInfo(){
        return info;
    }
}
