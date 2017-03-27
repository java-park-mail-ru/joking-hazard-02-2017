package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import sample.Models.LogInModel;
/**
 * Created by ksg on 18.03.17.
 */
public final class LogInData {
    final String userLogin;
    final String pass;
    public enum  ViewError {OK,INVALID_DATA_ERROR};
    @SuppressWarnings("unused")
    @JsonCreator
    public LogInData(@JsonProperty("userId") String userLogin, @JsonProperty("pass") String pass) {
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

    public ViewError valid(){
        return ViewError.OK;
    }

}