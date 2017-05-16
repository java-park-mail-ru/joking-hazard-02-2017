package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
import sample.Game.Messages.UserMessages.ChooseCardFromHand;

import java.util.Map;

/**
 * Created by ksg on 16.05.17.
 */
public class RoundState extends GameState {
    public RoundState(MainMechanics.GameContext context){
        this.context = context;
    }


    public ErrorCodes transfer() {
        this.context.state = this;
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            GameUserItem user = entry.getValue();
            if (user != context.master){
                user.getCardFromHand();
            }
        }
        return ErrorCodes.OK;
    }

    private ErrorCodes chooseCardFromHand(BaseMessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        if(item == context.master){
            return ErrorCodes.BAD_BEHAVIOR;
        }
        if (context.table.containsKey(userId)){
            return ErrorCodes.BAD_BEHAVIOR;
        }
        Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        ChooseCardFromHand conMessage = (ChooseCardFromHand) cls.cast(msg.getMsg(context.mapper));
        //TODO: Logic
        int index = conMessage.getChosenCard();
        GameCard card = item.getCardFromHandByIndex(index);
        if(card == null){
            return ErrorCodes.OUT_OF_RANGE;
        }
        context.table.put(userId,card);
        if ((context.table.size() + 1) >= context.mp.size()){
            RoundFinishState state = new RoundFinishState(context);
            return state.transfer();
        }
        return ErrorCodes.OK;
    }

    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        BaseGameMessage ser_msg = msg.getMsg(context.mapper);
        if(ser_msg == null){
            return ErrorCodes.SERIALIZATION_ERROR;
        }
        String type = ser_msg.getType();
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