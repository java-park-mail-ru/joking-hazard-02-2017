package sample.Services;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sample.DAO.AccountDAO;
import sample.Models.LogInModel;
import sample.Models.UserInfoModel;
import sample.Models.UserData;

@Service
public class AccountService {
    public enum  ErrorCodes {
        OK,
        INVALID_LOGIN,
        INVALID_PASSWORD,
        LOGIN_OCCUPIED,
        INVALID_AUTH_DATA,
        INVALID_REG_DATA,
        INVALID_SESSION,
        DATABASE_ERROR,
        SERVER_ERROR
    }

    private final AccountDAO accountDAO;

    @Autowired
    public AccountService(final JdbcTemplate jdbcTemplate) {
        this.accountDAO = new AccountDAO(jdbcTemplate);
    }

    @SuppressWarnings("unused")
    @NotNull
    public ErrorCodes register(@NotNull UserData data) {
        if (data.getUserLogin() == null || data.getUserMail() == null || data.getPassHash() == null) {
            return ErrorCodes.INVALID_REG_DATA;
        }

        try {
            accountDAO.insertUserIntoDb(data);

        } catch (DuplicateKeyException ex) {
            return ErrorCodes.LOGIN_OCCUPIED;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;

        } catch (Exception ex) {
            return ErrorCodes.SERVER_ERROR;
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
            final String hash = record.getPassHash();

            if (!hash.equals(data.getPassHash())) {
                return ErrorCodes.INVALID_PASSWORD;
            }

        } catch (EmptyResultDataAccessException ex) {
            return  ErrorCodes.INVALID_LOGIN;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;

        } catch (Exception ex) {
            return ErrorCodes.SERVER_ERROR;
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

        } catch (Exception ex) {
            return ErrorCodes.SERVER_ERROR;
        }

        return ErrorCodes.OK;
    }

    public ErrorCodes changePassHash(@NotNull  String newPassHash, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            data.setPassHash(newPassHash);
            accountDAO.changeUserPass(data);

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_SESSION;

        } catch (DataAccessException ex) {
            return ErrorCodes.DATABASE_ERROR;

        } catch (Exception ex) {
            return ErrorCodes.SERVER_ERROR;
        }

        return ErrorCodes.OK;
    }

    public boolean checkPass(@NotNull String passHash, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            final String passH = data.getPassHash();

            if (!passHash.equals(passH)) {
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

        } catch (Exception ex) {
            return ErrorCodes.SERVER_ERROR;
        }

        return ErrorCodes.OK;
    }
}