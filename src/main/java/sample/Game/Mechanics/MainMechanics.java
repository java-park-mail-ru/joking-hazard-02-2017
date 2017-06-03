package sample.Game.Mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.States.GameState;
import sample.Game.Mechanics.States.InitState;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.RoundInfo;
import sample.Game.Messages.ServerMessages.ServerErrorMessage;
import sample.Game.Messages.ServerMessages.ServerFinishedMessage;
import sample.Game.Messages.SystemMessages.BaseSystemMessageContainer;
import sample.Game.Messages.SystemMessages.ErrorMessage;
import sample.Game.Messages.SystemMessages.UpdateScoreMessage;
import sample.Game.Services.DatabaseWorker;
import sample.Lobby.Views.LobbyGameView;
import sample.Lobby.Views.UserGameView;
import sample.ResourceManager.ResourceManager;

import java.util.*;

/**
 * Created by ksg on 25.04.17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MainMechanics {
    public enum ErrorCodes {
        SERVER_ERROR,
        @SuppressWarnings("unused")RESOURCE_ERROR,
        SERIALIZATION_ERROR,
        INVALID_COMMAND,
        @SuppressWarnings("unused")OUT_OF_RANGE,
        FINISHED,
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMechanics.class);

    private LobbyGameView view;
    private final GameContext context;
    private final DatabaseWorker database;

    public MainMechanics(ObjectMapper mapper,DatabaseWorker database) {
        context = new GameContext();
        context.mapper = mapper;
        this.database = database;
    }

    public ErrorCodes init(LobbyGameView view, ResourceManager manager) {
        LOGGER.debug("game created");
        this.view = view;
        final Vector<UserGameView> vtr = view.getList();
        context.deck = manager.getCardDeck();
        if (context.deck == null) {
            LOGGER.error("deck is null some trouble with resources");
            return ErrorCodes.SERVER_ERROR;
        }
        for (UserGameView cur : vtr) {
            if (cur == null) {
                LOGGER.error("null user error");
                return ErrorCodes.SERVER_ERROR;
                // if i'll do big lock in lobby - nevermore
            }
            final String id = cur.getUserId();
            context.numberCardsInHand = view.getCardsInHand();
            final Vector<GameCard> cards = context.deck.popCards(context.numberCardsInHand);//TODO: resource this
            if (cards == null) {
                LOGGER.error("deck is null some trouble with resources");
                return ErrorCodes.SERVER_ERROR;
            }
            final GameUserItem curPlayer = new GameUserItem(cards, context.mapper);
            context.mp.put(id, curPlayer);
            context.masterQeue.add(curPlayer);
        }
        context.numberOfPlayers = view.getNumber();
        return ErrorCodes.OK;
    }

    public RoundInfo getRoundInfo(){
        final Vector<GameUserInfo> resvect = new Vector<>();
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final String userId = entry.getKey();
            final GameUserItem user = entry.getValue();
            resvect.add(new GameUserInfo(context.mapper, userId, false, user.getScore()));
        }
        return new RoundInfo(context.mapper, resvect, context.currentRound);
    }

    public ErrorCodes handleMessage(BaseMessageContainer msg) {
        final GameState.ErrorCodes err = context.state.handle(msg);
        return errorHandler(err, msg.getUserId());
    }

    private ErrorCodes scoreUpdated(){
        return  ErrorCodes.FINISHED;
    }

    private ErrorCodes error(){
        ServerErrorMessage msg = new ServerErrorMessage(context.mapper, "Error of scoreUpdate");
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.sendMessage(msg);
        }
        return ErrorCodes.FINISHED;
    }

    public ErrorCodes handleSystemMessage(BaseSystemMessageContainer msg) {
        String type = msg.getMsg().getType();
        switch (type){
            case "ScoreUpdated":{
                return this.scoreUpdated();
            }
            case "Error":{
                return this.error();
            }
        }
        return ErrorCodes.OK;
    }

    private ErrorCodes errorHandler(GameState.ErrorCodes err, String userId) {
        final GameUserItem item = context.mp.get(userId);
        switch (err) {
            case OK: {
                break;
            }
            case INVALID_COMMAND: {
                item.sendMessage(new ServerErrorMessage(context.mapper, "Invalid Command"));
                return ErrorCodes.INVALID_COMMAND;
            }
            case SERIALIZATION_ERROR: {
                item.sendMessage(new ServerErrorMessage(context.mapper, "Serialization Error"));
                return ErrorCodes.INVALID_COMMAND;
            }
            case OUT_OF_RANGE: {
                item.sendMessage(new ServerErrorMessage(context.mapper, "Out of range Error"));
                return ErrorCodes.SERIALIZATION_ERROR;
            }

            case BAD_BEHAVIOR: {
                item.sendMessage(new ServerErrorMessage(context.mapper, "Unexpected behavior"));
                return ErrorCodes.INVALID_COMMAND;
            }
            case SERVER_ERROR: {
                LOGGER.info("SERVER ERROR \n");
                for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
                    entry.getValue().sendMessage(new ServerErrorMessage(context.mapper, "server error, closing game"));
                }
                return ErrorCodes.FINISHED;
            }
            case FINISHED: {
                LOGGER.info("GameFinished\n");
                RoundInfo roundInfo = this.getRoundInfo();
                UpdateScoreMessage msg = new UpdateScoreMessage(roundInfo);
                this.database.getQue().add(msg);
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.OK;
    }

    public LobbyGameView getUsersView() {
        return view;
    }

    public void finishGame() {
        final RoundInfo msg = this.getRoundInfo();
        final ServerFinishedMessage finmsg = new ServerFinishedMessage(context.mapper);
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            final GameUserItem user = entry.getValue();
            user.sendMessage(msg);
            entry.getValue().sendMessage(finmsg);
        }
        for (Map.Entry<String, GameUserItem> entry : context.mp.entrySet()) {
            entry.getValue().close();
        }
    }

}
