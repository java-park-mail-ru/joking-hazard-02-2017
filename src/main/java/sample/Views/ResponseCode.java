package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 02.03.17.
 */
public final class ResponseCode<T> {
    private final boolean result;
    private final String errorMsg;
    private final T data;

    @JsonCreator
    public ResponseCode(@JsonProperty("result") boolean result,
                        @JsonProperty("errorMsg") String errorMsg,
                        @JsonProperty("data") T data) {
        this.result = result;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    @JsonCreator
    public ResponseCode(@JsonProperty("result") boolean result,
                        @JsonProperty("errorMsg") String errorMsg) {
        this.result = result;
        this.errorMsg = errorMsg;
        this.data = null;
    }

    @SuppressWarnings("unused")
    public boolean getResult() {
        return result;
    }

    @SuppressWarnings("unused")
    public String getErrorMsg() {
        return errorMsg;
    }

    @SuppressWarnings("unused")
    public T getData() {
        return data;
    }
}