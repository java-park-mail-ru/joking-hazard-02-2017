package sample.Game.Mechanics.States;

import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.UserMessages.ChooseCardFromHand;
import sample.Game.Messages.UserMessages.ChooseCardFromTable;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by ksg on 16.05.17.
 */
public class RoundFinishState extends GameState{
    public RoundFinishState(MainMechanics.GameContext context){
        this.context = context;
    }


    public GameState.ErrorCodes transfer() {
        this.context.state = this;
        context.master.getCardFromTable();
        return GameState.ErrorCodes.OK;
    }

    private GameState.ErrorCodes chooseCardFromTable(BaseMessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        if(item != context.master){
            return GameState.ErrorCodes.BAD_BEHAVIOR;
        }
        if (context.table.containsKey(userId)){
            return GameState.ErrorCodes.BAD_BEHAVIOR;
        }
        Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        ChooseCardFromTable conMessage = (ChooseCardFromTable) cls.cast(msg.getMsg(context.mapper));
        //TODO: Logic
        int index = conMessage.getChosenCard();
        Iterator<Map.Entry<String,GameCard>> it = context.table.entrySet().iterator();
        for(int i = 0; i< index; i++){
            it.next();
        }
        String chosenUserId = it.next().getKey();
        if(chosenUserId == null){
            return GameState.ErrorCodes.OUT_OF_RANGE;
        }
        GameUserItem winner = context.mp.get(chosenUserId);
        winner.incrementScore();
        RoundBeginState state = new RoundBeginState(context);
        return state.transfer();
    }

    @Override
    public GameState.ErrorCodes handle(BaseMessageContainer msg) {
        BaseGameMessage ser_msg = msg.getMsg(context.mapper);
        if(ser_msg == null){
            return GameState.ErrorCodes.SERIALIZATION_ERROR;
        }
        String type = ser_msg.getType();
        switch (type) {
            case "ChooseCardFromTable": {
                return chooseCardFromTable(msg);
            }
            case "UserConnected": {
                return addUser(msg);
            }
            default:
                break;
        }
        return GameState.ErrorCodes.INVALID_COMMAND;
    }
}