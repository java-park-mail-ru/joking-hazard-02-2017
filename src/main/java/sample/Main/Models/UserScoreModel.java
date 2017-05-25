package sample.Main.Models;

/**
 * Created by lieroz on 24.05.17.
 */
public class UserScoreModel {
    private String login;
    private Integer score;

    public UserScoreModel(final String login, final Integer score) {
        this.login = login;
        this.score = score;
    }

    public String getLogin() {
        return this.login;
    }

    public Integer getScore() {
        return this.score;
    }
}
