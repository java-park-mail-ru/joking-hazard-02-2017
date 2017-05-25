package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Services.ServerManager;

import java.io.IOException;

/**
 * Created by ksg on 15.05.17.
 */
public class UserMessageContainer extends BaseMessageContainer {
    final String msg;
    BaseGameMessage res;

    public UserMessageContainer(String userId, ServerManager.GameIndex index, String msg) {
        super(userId, index);
        this.msg = msg;
        this.res = null;
    }

    @SuppressWarnings({"SameReturnValue", "unused"})
    public String getType() {
        return "UserMessageContainer";
    }

    @Nullable
    @Override
    public BaseGameMessage getMsg(ObjectMapper mapper) {
        //noinspection OverlyBroadCatchBlock
        try {
            if (res == null) {
                res = (BaseGameMessage) mapper.readValue(this.msg, BaseUserMessage.class);
            }
        } catch (@SuppressWarnings("OverlyBroadCatchBlock") IOException e) {
            res = null;
        }
        return res;
    }
}
