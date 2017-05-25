package sample.Lobby.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 12.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameReadyMessage extends BaseMessage {
    public GameReadyMessage(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public String getType() {
        return "GameReadyMessage";
    }
}
