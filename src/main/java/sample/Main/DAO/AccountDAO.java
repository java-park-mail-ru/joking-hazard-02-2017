package sample.main.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import sample.main.Models.UserData;

/**
 * Created by lieroz on 26.03.17.
 */

final public class AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final UserData getUserByLogin(final String login) {
        final String sql = "select * from users where login = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {login}, (rs, rowNum) ->
                new UserData(
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("password")
                )
        );
    }

    public final void insertUserIntoDb(final UserData userData) {
        final String sql = "insert into users (login, email, password) values(?, ?, ?)";
        jdbcTemplate.update(sql, userData.getUserLogin(),
                userData.getUserMail(), userData.getPassHash());
    }

    public final void changeUserMail(final UserData userData) {
        final String sql = "update users set email = ? where login = ?";
        jdbcTemplate.update(sql, userData.getUserMail(),
                userData.getUserLogin());
    }

    public final void changeUserPass(final UserData userData) {
        final String sql = "update users set password = ? where login = ?";
        jdbcTemplate.update(sql, userData.getPassHash(),
                userData.getUserLogin());
    }

    public final void deleteUserFromDb(final String login) {
        final String sql = "delete from users where login = ?";
        jdbcTemplate.update(sql, login);
    }
}
