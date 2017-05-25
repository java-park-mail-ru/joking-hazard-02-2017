package sample.Game.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 16.05.17.
 */
public abstract class BaseMessageContainer {
    protected final String userId;
    protected final ServerManager.GameIndex index;

    public BaseMessageContainer(String userId, ServerManager.GameIndex index) {
        this.userId = userId;
        this.index = index;
    }

    public String getUserId() {
        return userId;
    }

    public ServerManager.GameIndex getIndex() {
        return index;
    }

    public abstract BaseGameMessage getMsg(ObjectMapper mapper);
}
