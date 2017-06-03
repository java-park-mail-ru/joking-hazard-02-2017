package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.ServerErrorMessage;
import sample.Game.Messages.SystemMessages.*;
import sample.Game.Messages.UserMessages.UserMessageContainer;
import sample.Lobby.Messages.ErrorMessage;
import sample.Lobby.Services.LobbyService;
import sample.Lobby.Views.LobbyGameView;
import sample.Lobby.Views.UserGameView;
import sample.Main.Services.AccountService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ksg on 25.04.17.
 */


@SuppressWarnings("DefaultFileTemplate")
@Service
public class ServerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

    public enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        ERROR_GAME_CREATION,
        ERROR_GAME_CONNECTION,
    }

    public static class GameIndex {
        final int index;
        final int thread;

        public GameIndex(int thread, int index) {
            this.index = index;
            this.thread = thread;
        }

        public int getIndex() {
            return index;
        }

        public int getThread() {
            return thread;
        }
    }

    private final Map<String, GameIndex> indexMap = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ArrayList<ScheduledExecutorService> executors = new ArrayList<>();
    private final  ExecutorService databaseExecutor;
    private final DatabaseWorker databaseWorker;
    private final  AccountService accountService;
    private final ArrayList<ServerWorker> worker = new ArrayList<>(5);
    private AtomicInteger curThread = new AtomicInteger(0);
    private final int maxThreads = 5;

    public int getNew() {
        return curThread.incrementAndGet() % maxThreads;
    }

    public ServerManager(AccountService dataBase) {
        accountService = dataBase;
        databaseExecutor = Executors.newFixedThreadPool(1);
        ConcurrentLinkedQueue<BaseSystemMessage> databaseQue = new ConcurrentLinkedQueue<BaseSystemMessage>();
        databaseWorker = new DatabaseWorker(this, databaseQue, accountService);
        databaseExecutor.execute(databaseWorker);
        for (int i = 0; i < maxThreads; i++) {
            ConcurrentLinkedQueue<BaseMessageContainer> messageQue = new ConcurrentLinkedQueue<BaseMessageContainer>();
            ConcurrentLinkedQueue<BaseSystemMessageContainer> systemQue = new ConcurrentLinkedQueue<BaseSystemMessageContainer>();
            ScheduledExecutorService newExecutor = Executors.newScheduledThreadPool(1);
            ServerWorker wrk = new ServerWorker(this, messageQue, systemQue, newExecutor); // it'll be pull
            newExecutor.execute(wrk);
            worker.add(wrk);
            executors.add(newExecutor);
        }
    }

    public ErrorCodes createGame(LobbyGameView view) {
        int curr = getNew();
        final Integer ind = worker.get(curr).createGame(view);
        if (ind < 0) {
            return ErrorCodes.ERROR_GAME_CREATION;
        }
        final GameIndex index = new GameIndex(curr, ind);
        final Vector<UserGameView> ulist = view.getList();
        for (UserGameView user : ulist) {
            final String id = user.getUserId();
            indexMap.put(id, index);
        }
        return ErrorCodes.OK;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private ErrorCodes sendError(WebSocketSession session, String errorText) {
        final ServerErrorMessage err = new ServerErrorMessage(mapper, errorText);
        @SuppressWarnings({"unused", "LocalVariableNamingConvention"}) String ErrorText;
        try {
            session.sendMessage(new TextMessage(err.getJson()));
        } catch (IOException ignored) {
            LOGGER.error("IOException", ignored);
        }
        return ErrorCodes.OK;
    }

    //TODO: обернуть websocketsession
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes connectUser(WebSocketSession session) {//TODO: change work with session level
        final String userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            sendError(session, "No login");
            return ErrorCodes.ERROR_GAME_CONNECTION;
        }
        final GameIndex index = indexMap.get(userId);
        if (index == null) {
            sendError(session, "No login in game system");
            return ErrorCodes.ERROR_GAME_CONNECTION;
        }
        final UserConnectedMessage msg = new UserConnectedMessage(session);
        final MessageContainer container = new MessageContainer(userId, index, msg);
        worker.get(index.getThread()).getMessageQue().add(container);
        return ErrorCodes.OK;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes deleteUsers(LobbyGameView view) {
        final Vector<UserGameView> ulist = view.getList();
        for (UserGameView user : ulist) {
            final String id = user.getUserId();
            indexMap.remove(id);
        }
        return ErrorCodes.OK;
    }

    public void addMessage(WebSocketSession session, String userMessage) {
        final String userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            final ErrorMessage msg = new ErrorMessage("Invalid user session", mapper);
            @SuppressWarnings("LocalVariableNamingConvention") final String textMsg = msg.getJson();
            try {
                session.sendMessage(new TextMessage(textMsg));
            } catch (IOException ignored) {
                LOGGER.error("IOException", ignored);
            }
            return;
        }
        final GameIndex index = indexMap.get(userId);
        if (index == null) {
            sendError(session, "No login in game system");
            return;
        }
        UserMessageContainer container = new UserMessageContainer(userId, index, userMessage);
        worker.get(index.getThread()).getMessageQue().add(container);
    }

    public void addSystemMessage(String userId, BaseSystemMessage msg){
        final GameIndex index = indexMap.get(userId);
        if (index == null) {
            return;
        }
        final SystemMessageContainer container = new SystemMessageContainer(userId, index, msg);
        worker.get(index.getThread()).getSystemQue().add(container);
    }

    public boolean userExist(String userId) {
        return indexMap.containsKey(userId);
    }

    public DatabaseWorker getDatabase(){
        return this.databaseWorker;
    }
}
