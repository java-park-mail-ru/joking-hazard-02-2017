package sample.Main.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sample.Main.Views.UserInfo;

import javax.validation.constraints.NotNull;

/**
 * Created by ksg on 03.03.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class UserData {
    private final String userLogin;
    private String userMail;
    private String passHash;

    @JsonCreator
    public UserData(@JsonProperty("userMail") String userMail,
                    @JsonProperty("userLogin") String userLogin,
                    @JsonProperty("pass") String passHash) {
        this.userLogin = userLogin;
        this.passHash = passHash;
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassHash() {
        return passHash;
    }

    public String getUserMail() {
        return userMail;
    }

    @SuppressWarnings("unused")
    public UserInfo getUserInfo() {
        return new UserInfo(userMail, userLogin);
    }

    public void setUserMail(@NotNull String userMail) {
        this.userMail = userMail;
    }

    public void setPassHash(@NotNull String passHash) {
        this.passHash = passHash;
    }

}