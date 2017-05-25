package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BaseServerMessage {
    @JsonIgnore
    private final
    ObjectMapper mapper; //Make it static? or make fabric?

    BaseServerMessage(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unused")
    public String getType() {
        return "NoType";
    }

    @Nullable
    @JsonIgnore
    public String getJson() {
        String res = null;
        try {
            res = mapper.writeValueAsString(this);
        } catch (JsonProcessingException ignored) {
        }
        return res;
    }

}
