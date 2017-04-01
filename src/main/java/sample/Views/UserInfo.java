package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 17.03.17.
 */
public final class UserInfo {
    private final String userMail;
    private final String userLogin;

    @JsonCreator
    public UserInfo(@JsonProperty("userMail") String userMail,
                    @JsonProperty("userLogin") String userLogin) {
        this.userLogin = userLogin;
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserMail() {
        return userMail;
    }

}