package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 26.05.17.
 */
public class NewRoundMessage  extends BaseServerMessage {
    public NewRoundMessage(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public String getType() {
        return "NewRoundMessage";
    }
}