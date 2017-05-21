package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Lobby.Views.LobbyGameView;
import sample.ResourceManager.ResourceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ksg on 25.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServerWorker {//implements Runnable{

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

    public ServerWorker(ServerManager master) {
        manager = new ResourceManager();
        generator = new KeyGenerator();
        mapper = new ObjectMapper();
        games = new ConcurrentHashMap<>();
        this.master = master;
    }

    public Integer createGame(LobbyGameView players) {
        final MainMechanics game = new MainMechanics(mapper);
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

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes handleMessage(BaseMessageContainer container) {
        final Integer gameIndex = container.getIndex().getIndex();
        final MainMechanics mechanics = games.get(gameIndex);
        final MainMechanics.ErrorCodes err = mechanics.handleMessage(container);
        if (err == MainMechanics.ErrorCodes.FINISHED) {
            master.deleteUsers(mechanics.getUsersView());
            mechanics.finishGame();
        }
        return ErrorCodes.OK;
    }

    //public void run(){

    //}

}
