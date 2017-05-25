package sample.Lobby.Views;

import java.util.Vector;

/**
 * Created by ksg on 25.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LobbyGameView {
    private final Vector<UserGameView> list;
    private final int number;
    private final int cardsInHand;

    public LobbyGameView(Vector<UserGameView> list, int number, int cardsInHand) {
        this.list = list;
        this.number = number;
        this.cardsInHand = cardsInHand;
    }

    public Vector<UserGameView> getList() {
        return list;
    }

    public int getNumber() {
        return number;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

}
