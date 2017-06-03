package sample.Main.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by ksg on 03.03.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class UserData {
    private Integer id;
    private String userLogin;
    private String userMail;
    private String passHash;
    private Integer score;

    @JsonCreator
    public UserData(@JsonProperty("userMail") String userMail,
                    @JsonProperty("userLogin") String userLogin,
                    @JsonProperty("pass") String passHash) {
        this.userLogin = userLogin;
        this.passHash = passHash;
        this.userMail = userMail;
    }

    @JsonCreator
    public UserData(@JsonProperty("id") Integer id,
                    @JsonProperty("userLogin") String userLogin,
                    @JsonProperty("userMail") String userMail,
                    @JsonProperty("pass") String passHash,
                    @JsonProperty("score") Integer score) {
        this.id = id;
        this.userLogin = userLogin;
        this.passHash = passHash;
        this.userMail = userMail;
        this.score = score;
    }

    public Integer getId() {
        return id;
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

    public Integer getScore() {
        return score;
    }

    public void setUserMail(@NotNull String userMail) {
        this.userMail = userMail;
    }

    public void setPassHash(@NotNull String passHash) {
        this.passHash = passHash;
    }

}