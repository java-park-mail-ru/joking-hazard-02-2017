package sample.Lobby.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 12.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ErrorMessage extends BaseMessage {

    private final String errorMsg;

    public ErrorMessage(String errorMsg, ObjectMapper mapper) {
        super(mapper);
        this.errorMsg = errorMsg;
    }

    @Override
    public String getType() {
        return "ErrorMessage";
    }

    @SuppressWarnings("unused")
    public String getErrorMsg() {
        return errorMsg;
    }

}