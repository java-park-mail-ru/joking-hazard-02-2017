package sample.Main.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */

@SuppressWarnings("DefaultFileTemplate")
public final class LogInData {
    private final String userLogin;
    private final String pass;

    @SuppressWarnings("unused")
    public enum ViewError {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        INVALID_DATA_ERROR
    }

    @SuppressWarnings("unused")
    @JsonCreator
    public LogInData(@JsonProperty("userId") String userLogin,
                     @JsonProperty("pass") String pass) {
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