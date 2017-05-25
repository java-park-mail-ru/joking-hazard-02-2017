package sample.Lobby.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 24.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OkMessage extends BaseMessage {
    private final String okMsg;

    public OkMessage(@SuppressWarnings("SameParameterValue") String okMsg, ObjectMapper mapper) {
        super(mapper);
        this.okMsg = okMsg;
    }

    @Override
    public String getType() {
        return "OkMessage";
    }

    @SuppressWarnings("unused")
    public String getOkMsg() {
        return okMsg;
    }

}
