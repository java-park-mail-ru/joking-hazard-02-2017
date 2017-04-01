package sample.Views;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */

public final class MailForm {
    private String userMail;

    public MailForm(@JsonProperty("userMail") String userMail) {
        this.userMail = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
