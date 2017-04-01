package sample.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 17.03.17.
 */

public final class UserInfoModel {
    private String userMail;
    private String userLogin;

    @JsonCreator
    public UserInfoModel(@JsonProperty("userMail") String userMail,
                         @JsonProperty("userLogin") String userLogin) {
        this.userLogin = userLogin;
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(final String userLogin) {
        this.userLogin =  userLogin;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(final String userMail) {
        this.userMail = userMail;
    }


}