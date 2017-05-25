package sample.Game.Mechanics.Cards;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CardDeck {
    private final List<GameCard> container;

    public CardDeck(List<GameCard> deck) {
        container = new ArrayList<>(deck);
        Collections.shuffle(container);
    }

    @Nullable
    @SuppressWarnings("WeakerAccess")
    public GameCard popCard() {
        if (container.isEmpty()) {
            return null;
        }
        final GameCard card = container.get(0);
        container.remove(0);
        return card;
    }

    @Nullable
    public Vector<GameCard> popCards(int number) {
        final Vector<GameCard> res = new Vector<>();
        for (int i = 0; i < number; i++) {
            final GameCard card = popCard();
            if (card == null) {
                return null;
            }
            res.add(card);
        }
        return res;
    }
}
