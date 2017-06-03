package sample.Game.Messages.SystemMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 02.06.17.
 */
public abstract class BaseSystemMessageContainer {
    protected final String userId;
    protected final ServerManager.GameIndex index;

    public BaseSystemMessageContainer(String userId, ServerManager.GameIndex index) {
        this.userId = userId;
        this.index = index;
    }

    public String getUserId() {
        return userId;
    }

    public ServerManager.GameIndex getIndex() {
        return index;
    }

    public abstract BaseSystemMessage getMsg();
}

