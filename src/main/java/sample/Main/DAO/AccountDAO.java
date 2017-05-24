package sample.Main.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import sample.Main.Models.UserData;
import sample.Main.Models.UserScoreModel;

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
        final String sql = "SELECT login, email, password FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{login}, (rs, rowNum) ->
                new UserData(
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("password")
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

    public List<UserScoreModel> getScoreBoard(final String nickname) {
        final String sql = "SELECT login, score FROM users ORDER BY score, login";
        List<UserScoreModel> usersTable = jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserScoreModel(
                        rs.getString("login"),
                        rs.getInt("score")
                )
        );
        List<UserScoreModel> result = new ArrayList<>(usersTable.subList(0, usersTable.size() > 10 ? 10 : usersTable.size()));
        Boolean isPresent = false;
        for (int i = 0; i < result.size(); ++i) {
            if (result.get(i).getLogin().equals(nickname)) {
                isPresent = true;
                break;
            }
        }
        for (int i = 10; i < usersTable.size() && !isPresent; ++i) {
            if (usersTable.get(i).getLogin().equals(nickname)) {
                result.addAll(usersTable.subList(i - 1, i == usersTable.size() - 1 ? i + 1 : i + 2));
                break;
            }
        }
        return result;
    }
}
