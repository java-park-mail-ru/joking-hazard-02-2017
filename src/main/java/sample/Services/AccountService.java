package sample.Services;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
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
        INVALID_SESSION
    }

    @SuppressWarnings("unused")
    private MessageSource messageSource;
    private final AccountDAO accountDAO;

    @Autowired
    public AccountService(final MessageSource messageSource, final JdbcTemplate jdbcTemplate) {
        this.messageSource = messageSource;
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
        }

        return ErrorCodes.OK;
    }

    @SuppressWarnings("unused")
    public ErrorCodes changeMail(@NotNull String newMail, @NotNull String login) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            System.out.println(data.getUserLogin());
            data.setUserMail(newMail);
            accountDAO.changeUserMail(data);

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_SESSION;
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

        } catch (EmptyResultDataAccessException ex) {
            return false;
        }

        return true;
    }

    public ErrorCodes getUserData(@NotNull String login, UserInfoModel[] model) {
        try {
            final UserData data = accountDAO.getUserByLogin(login);
            model[0] = new UserInfoModel(data.getUserMail(), data.getUserLogin());

        } catch (EmptyResultDataAccessException ex) {
            return ErrorCodes.INVALID_LOGIN;
        }

        return ErrorCodes.OK;
    }
}