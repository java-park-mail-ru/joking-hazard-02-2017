package sample.Views;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */
public final class StringContainer {
    String strCont;

    @SuppressWarnings("unused")
    public StringContainer(@JsonProperty("strCont") String strCont) {
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
