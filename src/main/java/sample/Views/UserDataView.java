package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by ksg on 03.03.17.
 */
public final class UserDataView {
    public enum  ViewError {OK,INVALID_DATA_ERROR, }
    final String userLogin;
    String userMail;
    String pass;

    @SuppressWarnings("unused")
    @JsonCreator
    public UserDataView(@JsonProperty("userMail") String userMail,
                    @JsonProperty("userLogin") String userLogin,
                    @JsonProperty("pass") String pass) {
        this.userLogin = userLogin;
        this.pass = pass;
        this.userMail = userMail;
    }

    @SuppressWarnings("unused")
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

    public void setUserMail(@NotNull String userMail) {
        this.userMail = userMail;
    }

    public void setPass(@NotNull String pass) {
        this.pass = pass;
    }

    public ViewError valid(){
        return ViewError.OK;
    }

}