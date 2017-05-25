package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServerErrorMessage extends BaseServerMessage {
    private final String msg;

    public ServerErrorMessage(ObjectMapper mapper, String msg) {
        super(mapper);
        this.msg = msg;
    }

    @Override
    public String getType() {
        return "ErrorMsg";
    }

    @SuppressWarnings("unused")
    public String getMsg() {
        return msg;
    }
}
