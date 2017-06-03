package sample.Main.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * Created by lieroz on 2.06.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class UserInfoView {
    private Integer id;
    private String userLogin;
    private String userMail;
    private Integer userScore;

    public UserInfoView() {

    }

    @JsonCreator
    public UserInfoView(@Nullable @JsonProperty("id") Integer id,
                        @Nullable @JsonProperty("userLogin") String userLogin,
                        @Nullable @JsonProperty("userMail") String userMail,
                        @Nullable @JsonProperty("userScore") Integer userScore) {
        this.id = id;
        this.userLogin = userLogin;
        this.userMail = userMail;
        this.userScore = userScore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(final String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(final String userMail) {
        this.userMail = userMail;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(final Integer userScore) {
        this.userScore = userScore;
    }
}