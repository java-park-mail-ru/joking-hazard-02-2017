package sample.Views;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */

// Changed name to merge pull request
public final class MailForm {
    String strCont;

    @SuppressWarnings("unused")
    public MailForm(@JsonProperty("strCont") String strCont) {
        this.strCont = strCont;
    }

    @SuppressWarnings("unused")
    public String getStrCont() {
        return strCont;
    }

    @SuppressWarnings("unused")
    public void setStrCont(String strCont) {
        this.strCont = strCont;
    }
}
