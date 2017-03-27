package sample.Services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import sample.Controllers.LogInController;
import sample.Models.LogInModel;
import sample.Models.UserInfoModel;
import sample.Views.ResponseCode;
import sample.Models.UserData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import sample.Views.UserInfo;


@Service
public class AccountService {
    public enum  ErrorCodes{OK, INVALID_LOGIN, INVALID_PASSWORD, LOGIN_OCCUPIED, INVALID_AUTH_DATA, INVALID_SESSION}
    private final MessageSource messageSource;
    private final Map<String, UserData> userNameToUserProfile = new HashMap<>();

    @SuppressWarnings("unused")
    public AccountService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @SuppressWarnings("unused")
    @NotNull
    public ErrorCodes register(@NotNull UserData data) {
        final String login = data.getUserLogin();
        if (login == null) {
            return ErrorCodes.INVALID_LOGIN;
        }
        if (userNameToUserProfile.containsKey(login)) {
            return ErrorCodes.LOGIN_OCCUPIED;
        }
        userNameToUserProfile.put(login, data);
        return ErrorCodes.OK;
    }

    public ErrorCodes login(@NotNull LogInModel data) {
        final String login = data.getUserLogin();
        if (login == null) {
            return ErrorCodes.INVALID_AUTH_DATA;
        }
        final UserData record = userNameToUserProfile.get(login);
        if (record == null) {
            return  ErrorCodes.INVALID_LOGIN;
        }
        final String hash = record.getPassHash();
        if (!hash.equals(data.getPassHash())) {
            return ErrorCodes.INVALID_PASSWORD;
        }
        return ErrorCodes.OK;
    }

    @SuppressWarnings("unused")
    public ErrorCodes changeMail(@NotNull String newMail, @NotNull String login) {
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            return ErrorCodes.INVALID_SESSION;
        }
        data.setUserMail(newMail);
        return ErrorCodes.OK;
    }

    @SuppressWarnings("unused")
    public ErrorCodes changePassHash(@NotNull String newPassHash, @NotNull String login) {
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            return ErrorCodes.INVALID_SESSION;
        }
        data.setPassHash(newPassHash);
        return ErrorCodes.OK;
    }

    public boolean checkPass(@NotNull String passHash, @NotNull String login) {
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            return false;
        }
        final String passH = data.getPassHash();
        if (!passHash.equals(passH)) {
            return false;
        }
        return true;
    }


    @SuppressWarnings("unused")
    public ErrorCodes getUserData(@NotNull String login, UserInfoModel[] model) {
        final UserData data = userNameToUserProfile.get(login);
        if (data != null) {
            model[0] = new UserInfoModel(data.getUserMail(),data.getUserLogin());
            return ErrorCodes.OK;
        }
        return ErrorCodes.INVALID_LOGIN;
    }

}