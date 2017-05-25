package sample.Game.Mechanics.Cards;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameCard {
    private final int id;
    private final boolean red;

    public GameCard(int id, boolean red) {
        this.id = id;
        this.red = red;
    }

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public boolean getRed() {
        return red;
    }
}
