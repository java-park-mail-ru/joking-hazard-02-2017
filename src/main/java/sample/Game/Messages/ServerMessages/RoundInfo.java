package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Vector;

/**
 * Created by ksg on 14.05.17.
 */
public class RoundInfo extends BaseServerMessage {
    final int roundNum;
    final Vector<GameUserInfo> users;

    public RoundInfo(ObjectMapper mapper, Vector<GameUserInfo> users, int roundNum) {
        super(mapper);
        this.roundNum = roundNum;
        this.users = users;
    }

    @Override
    public String getType() {
        return "RoundInfo";
    }

    @SuppressWarnings("unused")
    public Vector<GameUserInfo> getUsers() {
        return users;
    }

    @SuppressWarnings("unused")
    public int getRoundNum() {
        return roundNum;
    }
}
