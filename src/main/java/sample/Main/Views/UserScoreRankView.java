package sample.Main.Views;

/**
 * Created by lieroz on 24.05.17.
 */
public class UserScoreRankView {
    private Integer rank;
    private String login;
    private Integer score;

    public UserScoreRankView(final Integer rank, final String login, final Integer score) {
        this.rank = rank;
        this.login = login;
        this.score = score;
    }

    public Integer getRank() {
        return this.rank;
    }

    public String getLogin() {
        return this.login;
    }

    public Integer getScore() {
        return this.score;
    }
}
