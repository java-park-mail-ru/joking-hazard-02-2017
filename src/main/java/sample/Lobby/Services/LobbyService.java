package sample.Lobby.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Services.ServerManager;
import sample.Lobby.Controllers.LobbyController;
import sample.Lobby.Controllers.LobbyUserController;
import sample.Lobby.Messages.ErrorMessage;
import sample.Lobby.Views.LobbyGameView;
import sample.Lobby.Views.LobbyView;
import sample.Main.Services.AccountService;
import sample.ResourceManager.ResourceManager;

/**
 * Created by ksg on 11.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
@Service
public class LobbyService {
    public enum ErrorCodes {
        DATABASE_ERROR,
        INVALID_LOGIN,
        SERVER_RESETED,
        INVALID_SESSION,
        SERVER_ERROR,
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private
    AccountService accountService;
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private
    ServerManager serverManager;
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private
    ResourceManager resourceManager;

    private final ObjectMapper mapper;

    private LobbyController currentLobby;

    private void resetLobby() {
        LOGGER.debug("Lobby reseted");
        if (currentLobby != null) {
            currentLobby.closeConnections();
        }
        currentLobby = new LobbyController(resourceManager.defaultMaxNumber(),
                resourceManager.numberOfCardsInHand(),
                mapper
        );
    }

    private void createLobby() {
        LOGGER.debug("Lobby created");
        currentLobby = new LobbyController(resourceManager.defaultMaxNumber(),
                resourceManager.numberOfCardsInHand(),
                mapper
        );
    }

    private void startGame() {
        //TODO: StartGame, retry to add new lobby
        currentLobby.gameStart();
        final LobbyGameView lb = currentLobby.getGameView();
        final ServerManager.ErrorCodes err = serverManager.createGame(lb);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (err) {
            case OK: {
                break;
            }
            case ERROR_GAME_CREATION: {
                currentLobby.sendMessageAll(new ErrorMessage("GameCreateionError", mapper));
            }
        }
        resetLobby();
    }

    public LobbyService(ObjectMapper mapper, ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.mapper = mapper;
        createLobby();
    }

    @SuppressWarnings({"UnusedReturnValue", "OverlyComplexMethod"})
    public synchronized ErrorCodes addUser(WebSocketSession userSession) {
        LOGGER.debug("User adding starts.");
        final LobbyUserController user = new LobbyUserController();
        final LobbyUserController.ErrorCodes initErr = user.lobbyUserControllerInit(userSession, accountService);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (initErr) {
            case OK: {
                LOGGER.debug("User inited");
                break;
            }
            case INVALID_LOGIN: {
                LOGGER.debug("Login is invalid");
                user.sendMessageToUser(new ErrorMessage("Invalid Login", mapper));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case DATABASE_ERROR: {
                LOGGER.debug("Database error");
                user.sendMessageToUser(new ErrorMessage("Database Error", mapper));
                user.close();
                return ErrorCodes.DATABASE_ERROR;
            }
            case INVALID_SESSION: {
                LOGGER.debug("Invalid session");
                user.sendMessageToUser(new ErrorMessage("Invalid Session", mapper));
                user.close();
                return ErrorCodes.INVALID_SESSION;
            }
            case SERVER_ERROR: {
                LOGGER.error("Server error in user Lobby Controller initialisation");
                user.sendMessageToUser(new ErrorMessage("Server Error", mapper));
                user.close();
                return ErrorCodes.SERVER_ERROR;
            }
        }
        if (serverManager.userExist(user.getUserId())) {
            user.sendMessageToUser(new ErrorMessage("Player with this login exist's in game", mapper));
            userSession.getAttributes().put("rejected", Boolean.TRUE);
            user.close();
            return ErrorCodes.INVALID_LOGIN;
        }
        final LobbyView lv = currentLobby.getView();
        user.sendMessageToUser(lv);
        final LobbyController.ErrorCodes err = currentLobby.addUser(user);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (err) {
            case OK: {
                LOGGER.debug("User added");
                break;
            }
            case LOBBY_IS_FOOL: {
                LOGGER.debug("Lobby is fool, game start init");
                startGame();
                break;
            }
            case LOGIN_EXIST: {
                user.sendMessageToUser(new ErrorMessage("Player with this login exist's in lobby", mapper));
                userSession.getAttributes().put("rejected", Boolean.TRUE);
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case INVALID_LOGIN: {
                LOGGER.debug("Login is invalid");
                user.sendMessageToUser(new ErrorMessage("Invalid Login", mapper));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case SERVER_ERROR: {
                LOGGER.error("Server error in user adding");
                user.sendMessageToUser(new ErrorMessage("Server Error", mapper));
                user.close();
                resetLobby();
                return ErrorCodes.SERVER_RESETED;
            }
        }
        return ErrorCodes.OK;
    }

    public synchronized ErrorCodes removeUser(String UserId) {
        LOGGER.debug("User removing starts");
        final LobbyController.ErrorCodes err = currentLobby.removeUser(UserId);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (err) {
            case OK: {
                break;
            }
            case INVALID_LOGIN: {
                return ErrorCodes.INVALID_LOGIN;
            }
        }
        LOGGER.debug("User removed");
        return ErrorCodes.OK;
    }
}
