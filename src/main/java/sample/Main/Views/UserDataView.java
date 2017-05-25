package sample.Main.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by ksg on 03.03.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class UserDataView {
    @SuppressWarnings("unused")
    public enum ViewError {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        INVALID_DATA_ERROR
    }

    private final String userLogin;
    private String userMail;
    private String pass;

    @SuppressWarnings("unused")
    @JsonCreator
    public UserDataView(@JsonProperty("userMail") String userMail,
                        @JsonProperty("userLogin") String userLogin,
                        @JsonProperty("pass") String pass) {
        this.userLogin = userLogin;
        this.pass = pass;
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPass() {
        return pass;
    }

    public String getUserMail() {
        return userMail;
    }

    @SuppressWarnings("unused")
    public UserInfo getUserInfo() {
        return new UserInfo(userMail, userLogin);
    }

    @SuppressWarnings("unused")
    public void setUserMail(@NotNull String userMail) {
        this.userMail = userMail;
    }

    @SuppressWarnings("unused")
    public void setPass(@NotNull String pass) {
        this.pass = pass;
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    public ViewError valid() {
        return ViewError.OK;
    }

}