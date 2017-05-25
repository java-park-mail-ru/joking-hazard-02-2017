package sample.Main.Services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sample.Main.DAO.AccountDAO;
import sample.Main.Models.LogInModel;
import sample.Main.Models.UserData;
import sample.Main.Models.UserInfoModel;
import sample.Main.Views.UserScoreRankView;

import java.util.List;

@Service
public class AccountService {
    public enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        INVALID_LOGIN,
        INVALID_PASSWORD,
        LOGIN_OCCUPIED,
        INVALID_AUTH_DATA,
        INVALID_REG_DATA,
        INVALID_SESSION,
        DATABASE_ERROR
    }

    private final AccountDAO accountDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(final JdbcTemplate jdbcTemplate) {
        this.accountDAO = new AccountDAO(jdbcTemplate);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("unused")
    @NotNull
    public ErrorCodes register(@NotNull UserData data) {
        LOGGER.info("User registered"); // TODO: more info in log
        if (data.getUserLogin() == null || data.getUserMail() == null || data.getPassHash() == null) {
            return ErrorCodes.INVALID_REG_DATA;
        }

        data.setPassHash(passwordEncoder().encode(data.getPassHash()));

        try {
            accountDAO.insertUserIntoDb(data);

        } catch (DuplicateKeyException ex) {
            return ErrorCodes.LOGIN_OCCUPIED;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public ErrorCodes login(@NotNull LogInModel data) {
        final String login = data.getUserLogin();

        if (login == null || data.getPassHash() == null) {
            return ErrorCodes.INVALID_AUTH_DATA;
        }

        try {
            final UserData record = accountDAO.getUserByLogin(login);

            if (!passwordEncoder().matches(data.getPassHash(), record.getPassHash())) {
                return ErrorCodes.INVALID_PASSWORD;
            }

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_LOGIN;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public ErrorCodes changeMail(@NotNull String newMail, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            System.out.println(data.getUserLogin());
            data.setUserMail(newMail);
            accountDAO.changeUserMail(data);

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_SESSION;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public ErrorCodes changePassHash(@NotNull String newPassHash, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            data.setPassHash(newPassHash);
            accountDAO.changeUserPass(data);

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_SESSION;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public boolean checkPass(@NotNull String passHash, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);

            if (!passwordEncoder().matches(passHash, data.getPassHash())) {
                return false;
            }

        } catch (DataAccessException ex) {
            return false;
        }

        return true;
    }

    public ErrorCodes getUserData(@NotNull String login, UserInfoModel model) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            model.setUserLogin(data.getUserLogin());
            model.setUserMail(data.getUserMail());

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_LOGIN;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public ErrorCodes deleteUserData(@NotNull String login) {
        try {
            accountDAO.deleteUserFromDb(login);

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_SESSION;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;
        }

        return ErrorCodes.OK;
    }

    public List<UserScoreRankView> getScoreBoard(@NotNull String login) {
        return accountDAO.getScoreBoard(login);
    }
}