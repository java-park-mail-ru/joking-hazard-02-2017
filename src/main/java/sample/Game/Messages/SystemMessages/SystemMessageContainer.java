package sample.Game.Messages.SystemMessages;

import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 02.06.17.
 */
public class SystemMessageContainer extends BaseSystemMessageContainer {
    BaseSystemMessage msg;
    public SystemMessageContainer(String userId, ServerManager.GameIndex index, BaseSystemMessage msg){
        super(userId, index);
        this.msg = msg;
    }

    @Override
    public BaseSystemMessage getMsg(){
        return msg;
    }
}
