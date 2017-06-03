package sample.Main.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import sample.Main.Models.UserData;
import sample.Main.Models.UserScoreModel;
import sample.Main.Views.UserScoreRankView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lieroz on 26.03.17.
 */


@SuppressWarnings("DefaultFileTemplate")
public final class AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserData getUserByLogin(final String login) {
        final String sql = "SELECT * FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{login}, (rs, rowNum) ->
                new UserData(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("score")
                )
        );
    }

    @SuppressWarnings("JavaDoc")
    public void insertUserIntoDb(final UserData userData) {
        final String sql = "INSERT INTO users (login, email, password) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, userData.getUserLogin(),
                userData.getUserMail(), userData.getPassHash());
    }

    public void changeUserMail(final UserData userData) {
        final String sql = "UPDATE users SET email = ? WHERE login = ?";
        jdbcTemplate.update(sql, userData.getUserMail(),
                userData.getUserLogin());
    }

    public void changeUserPass(final UserData userData) {
        final String sql = "UPDATE users SET password = ? WHERE login = ?";
        jdbcTemplate.update(sql, userData.getPassHash(),
                userData.getUserLogin());
    }

    public void deleteUserFromDb(final String login) {
        final String sql = "DELETE FROM users WHERE login = ?";
        jdbcTemplate.update(sql, login);
    }

    public List<UserScoreRankView> getScoreBoard(final String nickname) {
        final String sql = "SELECT login, score FROM users ORDER BY score DESC, login";
        List<UserScoreModel> usersTable = jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserScoreModel(
                        rs.getString("login"),
                        rs.getInt("score")
                )
        );
        List<UserScoreRankView> result = new ArrayList<>();
        Integer count = usersTable.size() > 10 ? 10 : usersTable.size();
        Boolean isPresent = false;
        for (int i = 0; i < count; ++i) {
            result.add(new UserScoreRankView(i + 1, usersTable.get(i).getLogin(), usersTable.get(i).getScore()));
            if (result.get(i).getLogin().equals(nickname)) {
                isPresent = true;
            }
        }
        for (int i = 10; i < usersTable.size() && !isPresent; ++i) {
            if (usersTable.get(i).getLogin().equals(nickname)) {
                if (i != 10) {
                    result.add(new UserScoreRankView(i, usersTable.get(i - 1).getLogin(), usersTable.get(i - 1).getScore()));
                }
                result.add(new UserScoreRankView(i + 1, usersTable.get(i).getLogin(), usersTable.get(i).getScore()));
                if (i != usersTable.size() - 1) {
                    result.add(new UserScoreRankView(i + 2, usersTable.get(i + 1).getLogin(), usersTable.get(i + 1).getScore()));
                }
                break;
            }
        }
        return result;
    }

    public void updateScore(final String login, final Integer score) {
        final String sql = "UPDATE users SET score = score + ? WHERE login = ?";
        jdbcTemplate.update(sql, score, login);
    }
}
