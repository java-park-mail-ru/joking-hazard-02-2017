package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PassForm {
    final String oldPassHash;
    final String newPassHash;

    @SuppressWarnings("unused")
    @JsonCreator
    public PassForm(@JsonProperty("oldPass") String oldPassHash,
                    @JsonProperty("newPass") String newPassHash) {
        this.oldPassHash = oldPassHash;
        this.newPassHash = newPassHash;
    }

    @SuppressWarnings("unused")
    public String getOldPassHash() {
        return oldPassHash;
    }

    @SuppressWarnings("unused")
    public String getNewPassHash() {
        return newPassHash;
    }
}