package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import sample.Models.UserInfoModel;
/**
 * Created by ksg on 17.03.17.
 */
public final class UserInfo {
    final String userMail;
    final String userLogin;

    @JsonCreator
    public UserInfo(@JsonProperty("userMail") String userMail,
                    @JsonProperty("userLogin") String userLogin) {
        this.userLogin = userLogin;
        this.userMail = userMail;
    }

    @SuppressWarnings("unused")
    public String getUserLogin() {
        return userLogin;
    }

    @SuppressWarnings("unused")
    public String getUserMail() {
        return userMail;
    }

}