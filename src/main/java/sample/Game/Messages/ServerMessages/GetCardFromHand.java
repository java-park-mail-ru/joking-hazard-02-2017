package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 14.05.17.
 */
public class GetCardFromHand extends BaseServerMessage {
    public GetCardFromHand(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public String getType() {
        return "GetCardFromHand";
    }
}
