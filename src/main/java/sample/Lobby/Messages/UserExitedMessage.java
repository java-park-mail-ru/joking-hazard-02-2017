package sample.Lobby.Messages;


import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Main.Views.UserInfo;

/**
 * Created by ksg on 12.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserExitedMessage extends BaseMessage {

    private final UserInfo view;

    public UserExitedMessage(UserInfo view, ObjectMapper mapper) {
        super(mapper);
        this.view = view;
    }

    @Override
    public String getType() {
        return "UserExitedMessage";
    }

    @SuppressWarnings("unused")
    public UserInfo getUser() {
        return view;
    }

}
