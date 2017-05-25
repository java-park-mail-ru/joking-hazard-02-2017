package sample.Lobby.Views;

/**
 * Created by ksg on 25.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserGameView {
    private final String UserLogin;

    public UserGameView(String UserLogin) {
        this.UserLogin = UserLogin;
    }

    public String getUserId() {
        return this.UserLogin;//пока просто заглушка
    }

    @SuppressWarnings("unused")
    public String getUserLogin() {
        return this.UserLogin;
    }
}
