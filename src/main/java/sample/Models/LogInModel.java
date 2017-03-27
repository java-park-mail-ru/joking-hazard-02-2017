package sample.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */
public final class LogInModel {
    final String userLogin;
    final String pass;

    @SuppressWarnings("unused")
    @JsonCreator
    public LogInModel(@JsonProperty("userId") String userLogin, @JsonProperty("pass") String pass) {
        this.userLogin = userLogin;
        this.pass = pass;
    }

    @SuppressWarnings("unused")
    public String getUserLogin() {
        return userLogin;
    }

    public String getPassHash() {
        return pass;
    }
}