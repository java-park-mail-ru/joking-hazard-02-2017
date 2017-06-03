package sample.Game.Mechanics.States;

import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.TableInfo;
import sample.Game.Messages.ServerMessages.UsersCardsInfo;
import sample.Game.Messages.UserMessages.ChooseCardFromTable;
import sample.Game.Mechanics.GameContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ksg on 16.05.17.
 */
public class RoundFinishState extends GameState {
    public RoundFinishState(GameContext context) {
        this.context = context;
    }


    @Override
    public GameState.ErrorCodes transfer() {
        this.context.state = this;
        BaseServerMessage msg = new UsersCardsInfo(context.mapper,
                new ArrayList<GameCard>( context.table.values()));
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.resetMessage();
            user.sendMessage(msg);
        }
        context.master.getCardFromTable();
        return GameState.ErrorCodes.OK;
    }

    private GameState.ErrorCodes chooseCardFromTable(BaseMessageContainer msg) {
        final String userId = msg.getUserId();
        final GameUserItem item = context.mp.get(userId);
        if (!Objects.equals(item, context.master)) {
            return GameState.ErrorCodes.BAD_BEHAVIOR;
        }
        if (context.table.containsKey(userId)) {
            return GameState.ErrorCodes.BAD_BEHAVIOR;
        }
        final Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        final ChooseCardFromTable conMessage = (ChooseCardFromTable) cls.cast(msg.getMsg(context.mapper));
        final int index = conMessage.getChosenCard();
        final Iterator<Map.Entry<String, GameCard>> it = context.table.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        Map.Entry<String, GameCard> wnr = it.next();
        if (wnr == null) {
            return GameState.ErrorCodes.OUT_OF_RANGE;
        }
        final String chosenUserId = wnr.getKey();
        context.cards[2]= wnr.getValue();
        final GameUserItem winner = context.mp.get(chosenUserId);
        winner.incrementScore();
        TableInfo tblInf = new TableInfo(context.mapper, context.cards);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.sendMessage(tblInf);
        }
        final RoundBeginState state = new RoundBeginState(context);
        return state.transfer();
    }

    @Override
    public GameState.ErrorCodes handle(BaseMessageContainer msg) {
        @SuppressWarnings("LocalVariableNamingConvention") final BaseGameMessage ser_msg = msg.getMsg(context.mapper);
        if (ser_msg == null) {
            return GameState.ErrorCodes.SERIALIZATION_ERROR;
        }
        final String type = ser_msg.getType();
        switch (type) {
            case "ChooseCardFromTable": {
                return chooseCardFromTable(msg);
            }
            case "UserConnected": {
                return addUser(msg);
            }
            case "UserExited": {
                return addUser(msg);
            }
            default:
                break;
        }
        return GameState.ErrorCodes.INVALID_COMMAND;
    }
}
