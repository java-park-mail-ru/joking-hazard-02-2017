package sample.Game.Mechanics.States;

import org.jetbrains.annotations.Nullable;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.NewRoundMessage;
import sample.Game.Messages.ServerMessages.RoundInfo;
import sample.Game.Messages.ServerMessages.TableInfo;
import sample.Game.Messages.UserMessages.ChooseCardFromHand;
import sample.Game.Mechanics.GameContext;

import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by ksg on 14.05.17.
 */
public class RoundBeginState extends GameState {


    public RoundBeginState(GameContext context) {
        this.context = context;
    }

    private void prepareTable(GameUserItem it) {
        final Vector<GameUserInfo> resvect = new Vector<>();
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final String userId = entry.getKey();
            final GameUserItem user = entry.getValue();
            resvect.add(new GameUserInfo(context.mapper, userId, (Objects.equals(it, user)), user.getScore()));
        }
        final RoundInfo msg = new RoundInfo(context.mapper, resvect, context.currentRound);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.sendMessage(msg);
        }
    }

    private void prepateCards() {
        final GameCard card = context.deck.popCard();
        if (!card.getRed()) {
            context.cards[1] = card;
        } else {
            context.cards[0] = card;
        }

        final TableInfo tableInfoMsg = new TableInfo(context.mapper, context.cards);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.sendHand();
            user.sendMessage(tableInfoMsg);
        }
    }

    private void addMasterCard(GameCard card) {
        if (context.cards[1] == null) {
            context.cards[1] = card;
        } else {
            context.cards[0] = card;
        }
    }


    @Nullable
    private GameUserItem getMaster() {
        GameUserItem it = context.masterQeue.poll();
        while ((it != null) && (!it.isUser())) {
            it = context.masterQeue.poll();
        }
        return it;
    }


    private ErrorCodes chooseCardFromHand(BaseMessageContainer msg) {
        final String userId = msg.getUserId();
        final GameUserItem item = context.mp.get(userId);
        if (!Objects.equals(item, context.master)) {
            return ErrorCodes.BAD_BEHAVIOR;
        }
        final Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        final ChooseCardFromHand conMessage = (ChooseCardFromHand) cls.cast(msg.getMsg(context.mapper));
        final int index = conMessage.getChosenCard();
        final GameCard card = context.master.getCardFromHandByIndex(index);
        if (card == null) {
            return ErrorCodes.OUT_OF_RANGE;
        }
        addMasterCard(card);
        final RoundState state = new RoundState(context);
        return state.transfer();
    }

    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        @SuppressWarnings("LocalVariableNamingConvention") final BaseGameMessage ser_msg = msg.getMsg(context.mapper);
        if (ser_msg == null) {
            return ErrorCodes.SERIALIZATION_ERROR;
        }
        final String type = ser_msg.getType();
        switch (type) {
            case "UserConnected": {
                return addUser(msg);
            }
            case "ChooseCardFromHand": {
                return chooseCardFromHand(msg);
            }
            case "UserExited": {
                return exitUser(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }

    @Override
    public ErrorCodes transfer() {
        context.reset();
        context.currentRound++;
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.resetMessage();
            if (!user.reget(context.deck)) {
                return ErrorCodes.SERVER_ERROR;
            }
            final NewRoundMessage newRoundMessage = new NewRoundMessage(context.mapper);
            user.sendMessage(newRoundMessage);
        }
        context.master = getMaster();
        if (context.master == null) {
            final FinishState state = new FinishState(context);
            return state.transfer();
        }
        prepareTable(context.master);
        prepateCards();
        context.master.getCardFromHand();
        this.context.state = this;
        return ErrorCodes.OK;
    }
}