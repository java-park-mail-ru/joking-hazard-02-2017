package sample.Game.Messages.SystemMessages;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserConnectedMessage extends BaseSystemMessage {
    private final WebSocketSession session;

    public UserConnectedMessage(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public String getType() {
        return "UserConnected";
    }

    public WebSocketSession getSession() {
        return session;
    }

    @Override
    public Class getClassOfMessage() {
        return UserConnectedMessage.class;
    }
}
