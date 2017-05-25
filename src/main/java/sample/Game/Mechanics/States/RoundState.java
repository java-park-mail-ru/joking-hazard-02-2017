package sample.Game.Mechanics.States;

import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.TableInfo;
import sample.Game.Messages.UserMessages.ChooseCardFromHand;
import sample.Game.Mechanics.GameContext;

import java.util.Map;
import java.util.Objects;

/**
 * Created by ksg on 16.05.17.
 */
public class RoundState extends GameState {
    public RoundState(GameContext context) {
        this.context = context;
    }


    @Override
    public ErrorCodes transfer() {
        this.context.state = this;
        TableInfo tblInf = new TableInfo(context.mapper, context.cards);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            if (!Objects.equals(user, context.master)) {
                user.getCardFromHand();
            }
            user.sendMessage(tblInf);
            user.resetMessage();
        }
        return ErrorCodes.OK;
    }

    private ErrorCodes chooseCardFromHand(BaseMessageContainer msg) {
        final String userId = msg.getUserId();
        final GameUserItem item = context.mp.get(userId);
        if (Objects.equals(item, context.master)) {
            return ErrorCodes.BAD_BEHAVIOR;
        }
        if (context.table.containsKey(userId)) {
            return ErrorCodes.BAD_BEHAVIOR;
        }
        final Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        final ChooseCardFromHand conMessage = (ChooseCardFromHand) cls.cast(msg.getMsg(context.mapper));
        final int index = conMessage.getChosenCard();
        final GameCard card = item.getCardFromHandByIndex(index);
        if (card == null) {
            return ErrorCodes.OUT_OF_RANGE;
        }
        context.table.put(userId, card);
        if ((context.table.size() + 1) >= context.mp.size()) {
            final RoundFinishState state = new RoundFinishState(context);
            return state.transfer();
        }
        return ErrorCodes.OK;
    }

    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        @SuppressWarnings("LocalVariableNamingConvention") final BaseGameMessage ser_msg = msg.getMsg(context.mapper);
        if (ser_msg == null) {
            return ErrorCodes.SERIALIZATION_ERROR;
        }
        final String type = ser_msg.getType();
        switch (type) {
            case "ChooseCardFromHand": {
                return chooseCardFromHand(msg);
            }
            case "UserConnected": {
                return addUser(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }
}
