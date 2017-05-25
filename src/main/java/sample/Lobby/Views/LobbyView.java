package sample.Lobby.Views;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Lobby.Messages.BaseMessage;
import sample.Main.Views.UserInfo;

import java.util.Vector;

/**
 * Created by ksg on 11.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LobbyView extends BaseMessage {
    private final Vector<UserInfo> users;
    private final int maxNumber;

    public LobbyView(ObjectMapper mapper, Vector<UserInfo> users, int maxNumber) {
        super(mapper);
        this.users = users;
        this.maxNumber = maxNumber;
    }

    @Override
    public String getType() {
        return "Lobby Info";
    }

    @SuppressWarnings("unused")
    public Vector<UserInfo> getUsers() {
        return users;
    }

    @SuppressWarnings("unused")
    public int getMaxNumber() {
        return maxNumber;
    }
}
