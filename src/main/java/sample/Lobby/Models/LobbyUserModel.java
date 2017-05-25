package sample.Lobby.Models;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobby.Views.UserGameView;
import sample.Main.Models.UserInfoModel;
import sample.Main.Services.AccountService;

/**
 * Created by ksg on 13.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LobbyUserModel {
    public enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        INVALID_LOGIN,
        DATABASE_ERROR,
        SERVER_ERROR
    }

    private final WebSocketSession session;
    private UserInfoModel userInfo;
    private final AccountService accountService;

    public LobbyUserModel(WebSocketSession session, AccountService accountService) {
        this.accountService = accountService;
        this.session = session;
        this.userInfo = null;
    }

    public ErrorCodes lobbyUserModelInit(String userId) {
        final UserInfoModel userInfoModel = new UserInfoModel(null, null);
        final AccountService.ErrorCodes resp = accountService.getUserData(userId, userInfoModel);
        //noinspection EnumSwitchStatementWhichMissesCases
        switch (resp) {
            case OK: {
                break;
            }
            case INVALID_LOGIN: {
                return ErrorCodes.INVALID_LOGIN;
            }
            case DATABASE_ERROR: {
                return ErrorCodes.DATABASE_ERROR;
            }
            default: {
                return ErrorCodes.SERVER_ERROR;
            }
        }
        this.userInfo = userInfoModel;
        return ErrorCodes.OK;
    }

    public String getUserId() {
        return userInfo.getUserLogin();
    }

    public WebSocketSession getSession() {
        return session;
    }

    public UserInfoModel getUserInfo() {
        return userInfo;
    }

    @Nullable
    public UserGameView getGameView() {
        if (userInfo == null) {
            return null;
        }
        final String login = userInfo.getUserLogin();
        return new UserGameView(login);
    }
}
