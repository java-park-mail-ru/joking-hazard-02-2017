package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameContext;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.*;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class GameState {
    public enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        INVALID_COMMAND,
        FINISHED,
        SERIALIZATION_ERROR,
        OUT_OF_RANGE,
        BAD_BEHAVIOR,
        SERVER_ERROR
    }

    protected void notifyUser(GameUserItem item) {
        final TableInfo tableInfoMsg = new TableInfo(context.mapper, context.cards);
        item.sendMessage(tableInfoMsg);
        final Vector<GameUserInfo> resvect = new Vector<>();
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final String userId = entry.getKey();
            final GameUserItem user = entry.getValue();
            resvect.add(new GameUserInfo(context.mapper, userId, (Objects.equals(context.master, user)), user.getScore()));
        }
        final RoundInfo msg = new RoundInfo(context.mapper, resvect, context.currentRound);
        item.sendMessage(msg);
        BaseServerMessage ucardsMsg = new UsersCardsInfo(context.mapper, new ArrayList<GameCard>( context.table.values()));
        item.sendMessage(ucardsMsg);
        item.resendLast();
    }

    protected ErrorCodes addUser(BaseMessageContainer msg) {
        final String userId = msg.getUserId();
        final GameUserItem item = context.mp.get(userId);
        final Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        final UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg(context.mapper));
        final WebSocketSession session = conMessage.getSession();
        final GameUser user = new GameUser(session);
        item.setStrategy(user);
        notifyUser(item);
        return ErrorCodes.OK;
    }

    protected ErrorCodes exitUser(BaseMessageContainer msg){
        return ErrorCodes.FINISHED;
    }

    protected GameContext context;

    public abstract ErrorCodes handle(BaseMessageContainer msg);

    public abstract ErrorCodes transfer();
}
