package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Lobby.Services.LobbyService;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BaseServerMessage {
    @JsonIgnore
    private final
    ObjectMapper mapper; //Make it static? or make fabric?

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

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
            LOGGER.error("JsonProcessingException", ignored);
        }
        return res;
    }

}
