package sample.Main.Views;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lieroz on 25.05.17.
 */
public class CardView {
    private Integer id;
    private String url;

    public CardView(@NotNull final Integer id,
                    @NotNull final String url) {
        this.id = id;
        this.url = url;
    }

    public Integer getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }
}
