package sample.Main.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class LogInModel {
    private final String userLogin;
    private final String pass;

    @JsonCreator
    public LogInModel(@JsonProperty("userId") String userLogin, @JsonProperty("pass") String pass) {
        this.userLogin = userLogin;
        this.pass = pass;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassHash() {
        return pass;
    }
}