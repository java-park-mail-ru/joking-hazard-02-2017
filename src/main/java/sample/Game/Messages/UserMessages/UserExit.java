package sample.Game.Messages.UserMessages;

import sample.Game.Messages.BaseGameMessage;

/**
 * Created by ksg on 02.06.17.
 */
public class UserExit extends BaseGameMessage {
    public String getType() {
        return "UserExit";
    }
}
