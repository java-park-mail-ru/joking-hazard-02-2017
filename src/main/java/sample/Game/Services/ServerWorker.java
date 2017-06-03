package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.SystemMessages.BaseSystemMessage;
import sample.Game.Messages.SystemMessages.BaseSystemMessageContainer;
import sample.Lobby.Messages.BaseMessage;
import sample.Lobby.Views.LobbyGameView;
import sample.ResourceManager.ResourceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by ksg on 25.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServerWorker implements Runnable{

    enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,

    }

    private static class KeyGenerator {
        final int keyMod = 99999; // TODO: MAKE IT AS CONFIG
        int current;

        KeyGenerator() {
            current = 0;
        }

        public int getIndex() {
            current = (current + 1) % keyMod;
            return current;
        }
    }

    private final ResourceManager manager;
    private final KeyGenerator generator;
    private final ObjectMapper mapper;
    private final Map<Integer, MainMechanics> games;
    private final ServerManager master;
    private final ConcurrentLinkedQueue<BaseMessageContainer> messageQue;
    private final ConcurrentLinkedQueue<BaseSystemMessageContainer> systemQue;
    ServerManager serverManager;
    ScheduledExecutorService executorService;

    public ServerWorker(ServerManager master, ConcurrentLinkedQueue<BaseMessageContainer> messageQueue,
                        ConcurrentLinkedQueue<BaseSystemMessageContainer> systemQue,
                        ScheduledExecutorService executorService) {
        manager = new ResourceManager();
        generator = new KeyGenerator();
        mapper = new ObjectMapper();
        games = new ConcurrentHashMap<>();
        this.master = master;
        this.messageQue = messageQueue;
        this.systemQue = systemQue;
        this.executorService = executorService;
    }

    public ConcurrentLinkedQueue<BaseMessageContainer> getMessageQue(){
        return messageQue;
    }

    public ConcurrentLinkedQueue<BaseSystemMessageContainer> getSystemQue(){
        return systemQue;
    }

    private void  deleteGame(Integer index){
        games.remove(index);
    }
    public Integer createGame(LobbyGameView players) {
        final MainMechanics game = new MainMechanics(mapper,this.master.getDatabase());
        final MainMechanics.ErrorCodes error = game.init(players, manager);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (error) {
            case OK: {
                break;
            }
            case SERVER_ERROR: {
                return -1;
            }
        }
        final Integer key = generator.getIndex();
        games.put(key, game);
        return key;
    }

    private void handleMessage(BaseMessageContainer container) {
        final Integer gameIndex = container.getIndex().getIndex();
        final MainMechanics mechanics = games.get(gameIndex);
        final MainMechanics.ErrorCodes err = mechanics.handleMessage(container);
        if (err == MainMechanics.ErrorCodes.FINISHED) {
            master.deleteUsers(mechanics.getUsersView());
            mechanics.finishGame();
            deleteGame(gameIndex);
        }
    }

    private void handleSystemMessage(BaseSystemMessageContainer container){
        final Integer gameIndex = container.getIndex().getIndex();
        final MainMechanics mechanics = games.get(gameIndex);
        final MainMechanics.ErrorCodes err = mechanics.handleSystemMessage(container);
        if (err == MainMechanics.ErrorCodes.FINISHED) {
            master.deleteUsers(mechanics.getUsersView());
            mechanics.finishGame();
            deleteGame(gameIndex);
        }
        return;
    }

    @Override
    public void run(){ //TODO: reschedule
            if(!this.messageQue.isEmpty()){
                handleMessage(this.messageQue.poll());
            }
            if(!this.systemQue.isEmpty()){
                handleSystemMessage(this.systemQue.poll());
            }
            executorService.execute(this);
    }
}
