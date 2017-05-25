package sample.Game.Messages.SystemMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MessageContainer extends BaseMessageContainer {
    private final BaseGameMessage msg;

    public MessageContainer(String userId, ServerManager.GameIndex index, BaseGameMessage msg) {
        super(userId, index);
        this.msg = msg;
    }

    @Override
    public BaseGameMessage getMsg(ObjectMapper mapper) {
        return msg;
    }
}
