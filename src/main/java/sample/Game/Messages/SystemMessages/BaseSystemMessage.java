package sample.Game.Messages.SystemMessages;

import sample.Game.Messages.BaseGameMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BaseSystemMessage extends BaseGameMessage {
    @Override
    public String getType() {
        return "BaseSystemMessage";
    }

    @Override
    public Class getClassOfMessage() {
        return BaseSystemMessage.class;
    }
}
