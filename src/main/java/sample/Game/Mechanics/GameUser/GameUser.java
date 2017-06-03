package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.GetCardFromHand;
import sample.Game.Messages.ServerMessages.GetCardFromTable;
import sample.Game.Messages.ServerMessages.HandInfo;
import sample.Lobby.Services.LobbyService;

import java.io.IOException;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameUser implements GameUserInterface {
    private final WebSocketSession session;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

    public GameUser(WebSocketSession session) {
        this.session = session;
    }

    //for bots using in pattern strategy
    @Override
    public void init(HandInfo info) {
        send(info);
    }

    @Override
    public void sendHandInfo(HandInfo info) {
        send(info);
    }

    @Override
    public BaseServerMessage chooseCardFromHand(ObjectMapper mapper) {
        BaseServerMessage msg = new GetCardFromHand(mapper);
        send(msg);
        return msg;
    }

    @Override
    public BaseServerMessage chooseCardFromTable(ObjectMapper mapper) {
        BaseServerMessage msg = new GetCardFromTable(mapper);
        send(msg);
        return msg;
    }

    @Override
    public ErrorCodes send(BaseServerMessage msg) {
        try {
            session.sendMessage(new TextMessage(msg.getJson()));
        } catch (IOException ignored) {
            LOGGER.error("IOException", ignored);
        }
        return ErrorCodes.OK;
    }

    @Override
    public void close() {
        try {
            session.close();
        } catch (IOException ignored) {
            LOGGER.error("IOException", ignored);
        }
    }

    @Override
    public boolean isUser() {
        return true;
    }
}
