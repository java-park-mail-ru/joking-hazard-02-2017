package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 14.05.17.
 */
public class GameUserInfo extends BaseServerMessage {
    final String userLogin;
    final int score;
    final boolean isMaster;

    public GameUserInfo(ObjectMapper mapper, String userLogin, boolean isMaster, int score) {
        super(mapper);
        this.userLogin = userLogin;
        this.isMaster = isMaster;
        this.score = score;
    }

    @Override
    public String getType() {
        return "GameUserInfo";
    }

    @SuppressWarnings("unused")
    @JsonProperty(value = "isMaster")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "isMaster")
    @JsonIdentityReference(alwaysAsId = true)
    public boolean getIsMaster() {
        return isMaster;
    }

    @SuppressWarnings("unused")
    @JsonProperty(value = "userLogin")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userLogin")
    @JsonIdentityReference(alwaysAsId = true)
    public String getUserLogin() {
        return userLogin;
    }

    @SuppressWarnings("unused")
    @JsonProperty(value = "score")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "score")
    @JsonIdentityReference(alwaysAsId = true)
    public int getScore() {
        return score;
    }
}
