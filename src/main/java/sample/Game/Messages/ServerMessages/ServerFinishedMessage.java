package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 27.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServerFinishedMessage extends BaseServerMessage {
    public ServerFinishedMessage(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public String getType() {
        return "Game Finished Message";
    }
}
