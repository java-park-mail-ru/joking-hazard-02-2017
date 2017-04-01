package sample.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PassForm {
    private final String oldPassHash;
    private final String newPassHash;

    @SuppressWarnings("unused")
    @JsonCreator
    public PassForm(@JsonProperty("oldPass") String oldPassHash,
                    @JsonProperty("newPass") String newPassHash) {
        this.oldPassHash = oldPassHash;
        this.newPassHash = newPassHash;
    }

    public String getOldPassHash() {
        return oldPassHash;
    }

    public String getNewPassHash() {
        return newPassHash;
    }
}